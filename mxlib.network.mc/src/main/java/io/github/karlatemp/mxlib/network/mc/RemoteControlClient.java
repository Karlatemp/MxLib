/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-mc.main/RemoteControlClient.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.network.mc;

import io.github.karlatemp.mxlib.network.PipelineUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * RCON Client
 * <p>
 * How to use: See the `main(String[])` method in source
 */
public class RemoteControlClient {
    protected final EventLoopGroup loopGroup;
    protected final String host;
    protected final int port;
    protected final ByteBuf passwd;
    protected final AtomicBoolean ogMsgLock = new AtomicBoolean();
    protected final ConcurrentLinkedQueue<Msg> msgs = new ConcurrentLinkedQueue<>();
    protected final AtomicInteger reqIdSeq = new AtomicInteger();
    public boolean disconnected;
    protected int disconnectOvId = -1;

    public static class Msg {
        int requestId, type;
        ByteBuf payload;

        @Override
        public String toString() {
            return "Msg{" +
                    "requestId=" + requestId +
                    ", type=" + type +
                    ", payload=" + payload.toString(StandardCharsets.UTF_8) +
                    '}';
        }

        public static Msg newMsg(int requestId, int type, ByteBuf payload) {
            Msg msg = new Msg();
            msg.requestId = requestId;
            msg.type = type;
            msg.payload = payload;
            return msg;
        }

    }

    protected Channel channel;
    public Consumer<Msg> onMsgHandle;
    public Runnable onDisconnect;
    public Supplier<ReadTimeoutHandler> readTimeoutHandlerSupplier;
    public Supplier<WriteTimeoutHandler> writeTimeoutHandlerSupplier;

    public RemoteControlClient(EventLoopGroup loopGroup, String host, int port, ByteBuf passwd) {
        this.loopGroup = loopGroup;
        this.host = host;
        this.port = port;
        this.passwd = passwd;
    }

    protected void onDisconnect() throws Exception {
        if (onDisconnect != null)
            onDisconnect.run();
    }

    private static <T> T invokeSupplier(Supplier<T> supplier) {
        if (supplier == null) return null;
        return supplier.get();
    }

    protected ReadTimeoutHandler getReadTimeoutHandler() {
        ReadTimeoutHandler readTimeoutHandler = invokeSupplier(this.readTimeoutHandlerSupplier);
        if (readTimeoutHandler != null) return readTimeoutHandler;
        return new ReadTimeoutHandler(30000, TimeUnit.MILLISECONDS);
    }

    protected WriteTimeoutHandler getWriteTimeoutHandler() {
        WriteTimeoutHandler writeTimeoutHandler = invokeSupplier(this.writeTimeoutHandlerSupplier);
        if (writeTimeoutHandler != null) return writeTimeoutHandler;
        return new WriteTimeoutHandler(30000, TimeUnit.MILLISECONDS);
    }

    public void connect() throws InterruptedException, IOException {
        disconnected = false;
        {
            ogMsgLock.set(true);
            Channel oldChannel = this.channel;
            if (oldChannel != null && oldChannel.isActive()) {
                oldChannel.close();
            }
            channel = null;
        }
        CompletableFuture<Void> auth = new CompletableFuture<>();
        ChannelFuture future = new Bootstrap()
                .group(loopGroup)
                .channel(PipelineUtils.getChannel())
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline()
                                .addLast("read-timeout", getReadTimeoutHandler())
                                .addLast("write-timeout", getWriteTimeoutHandler())
                                .addLast("encoder", new MessageToByteEncoder<Msg>() {
                                    @Override
                                    public boolean acceptOutboundMessage(Object msg) throws Exception {
                                        return msg instanceof Msg;
                                    }

                                    @Override
                                    protected void encode(ChannelHandlerContext channelHandlerContext, Msg msg, ByteBuf byteBuf) throws Exception {
                                        byteBuf.ensureWritable(msg.payload.readableBytes() + 14);
                                        byteBuf.writeIntLE(msg.payload.readableBytes() + 10);
                                        byteBuf.writeIntLE(msg.requestId);
                                        byteBuf.writeIntLE(msg.type);
                                        byteBuf.writeBytes(msg.payload.copy());
                                        byteBuf.writeByte(0);
                                        byteBuf.writeByte(0);
                                    }
                                })
                                .addLast("decoder", new ByteToMessageDecoder() {
                                    @Override
                                    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
                                        while (byteBuf.isReadable(Integer.BYTES)) {
                                            byteBuf.markReaderIndex();
                                            int size = byteBuf.readIntLE();
                                            if (10 > size) {
                                                throw new DecoderException("Error response");
                                            }
                                            if (size > byteBuf.readableBytes()) {
                                                byteBuf.resetReaderIndex();
                                                return;
                                            }
                                            int rid = byteBuf.readIntLE();
                                            int type = byteBuf.readIntLE();
                                            ByteBuf payload = byteBuf.readBytes(size - 10);
                                            byteBuf.readByte();
                                            byteBuf.readByte();
                                            list.add(Msg.newMsg(rid, type, payload));
                                        }
                                    }
                                })
                                .addLast("receive", new ChannelInboundHandlerAdapter() {

                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        ctx.channel().writeAndFlush(
                                                Msg.newMsg(1, 3, passwd.duplicate())
                                        ).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                                    }

                                    @Override
                                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                        onDisconnect();
                                    }

                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
                                        if (obj instanceof Msg) {
                                            Msg msg = (Msg) obj;
                                            if (msg.type == 2) { // Auth
                                                if (msg.requestId == -1) {
                                                    auth.completeExceptionally(new IOException("Authentication failed"));
                                                    ctx.channel().close();
                                                    return;
                                                } else {
                                                    auth.complete(null);
                                                }
                                                ogMsgLock.set(false);
                                            } else {
                                                if (msg.requestId == disconnectOvId) {
                                                    disconnect();
                                                    return;
                                                }
                                                ogMsgLock.set(false);
                                                if (onMsgHandle != null) {
                                                    onMsgHandle.accept(msg);
                                                }
                                            }
                                            if (ogMsgLock.compareAndSet(false, true)) {
                                                Msg msg1 = msgs.poll();
                                                if (msg1 != null) {
                                                    ctx.channel().writeAndFlush(msg1).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                                                } else {
                                                    ogMsgLock.set(false);
                                                }
                                            }
                                            return;
                                        }
                                        super.channelRead(ctx, obj);
                                    }
                                })
                        ;
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, true)
                .connect(host, port)
                .sync();
        if (!future.isSuccess()) {
            throwIOE(future.cause());
        }
        channel = future.channel();
        try {
            auth.get();
        } catch (ExecutionException e) {
            throwIOE(e.getCause());
        }
    }

    public void sendMsg(Msg msg) {
        if (disconnected || channel == null || !channel.isActive()) {
            throw new RuntimeException("Channel closed.");
        }
        msgs.add(msg);
        if (ogMsgLock.compareAndSet(false, true)) {
            Msg msg1 = msgs.poll();
            if (msg1 != null) {
                channel.writeAndFlush(msg1).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            } else {
                ogMsgLock.set(false);
            }
        }
    }

    public void sendCmd(String cmd) {
        sendMsg(Msg.newMsg(reqIdSeq.getAndIncrement(), 2, Unpooled.wrappedBuffer(cmd.getBytes(StandardCharsets.UTF_8))));
    }

    public void disconnect() {
        disconnected = true;
        ogMsgLock.set(false);
        if (channel != null) {
            channel.close();
            channel = null;
        }
    }

    public void disconnectOnFinish() {
        sendMsg(Msg.newMsg(disconnectOvId = reqIdSeq.getAndIncrement(), 7, Unpooled.EMPTY_BUFFER));
    }

    private static void throwIOE(Throwable cause) throws IOException {
        if (cause instanceof IOException) throw (IOException) cause;
        if (cause instanceof RuntimeException) throw (RuntimeException) cause;
        if (cause instanceof Error) throw (Error) cause;
        throw new IOException(cause);
    }

    public static void main(String[] args) throws Throwable {
        ByteBuf passwd = Unpooled.wrappedBuffer("RCONPWD".getBytes(StandardCharsets.UTF_8));
        EventLoopGroup loopGroup = PipelineUtils.newEventLoopGroup();
        RemoteControlClient client = new RemoteControlClient(loopGroup, "localhost", 25575, passwd);
        client.readTimeoutHandlerSupplier = () -> new ReadTimeoutHandler(5, TimeUnit.SECONDS);
        client.onDisconnect = new Runnable() {
            int counter = 0;

            @Override
            public void run() {
                if (counter++ == 2) {
                    loopGroup.shutdownGracefully();
                } else {
                    try {
                        client.connect();
                        client.sendCmd("list");
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        client.connect();
        client.onMsgHandle = new Consumer<Msg>() {
            final AtomicInteger last = new AtomicInteger();

            @Override
            public void accept(Msg msg) {
                while (true) {
                    int l = last.get();
                    int requestId = msg.requestId;
                    if (last.compareAndSet(l, requestId)) {
                        if (l != requestId) {
                            System.out.println();
                        }
                        break;
                    }
                }
                System.out.print(msg.payload.toString(StandardCharsets.UTF_8));
            }
        };
        client.sendCmd("?");
        client.sendCmd("list");
        client.sendCmd("say 123456789");
        client.sendCmd("say 23qouiu4iq wui");
        client.sendCmd("say re er e er e  ewhoaiu 'a;sd asla WhoAmI   !@");
        client.sendCmd("me  Thank you");
        client.sendCmd("me  How are you");
        client.sendCmd("say Hello World!!!!");
        client.sendCmd("say Goodbye World!!!");
        // client.disconnectOnFinish();
    }
}
