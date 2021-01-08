package io.github.karlatemp.mxlib.network.mc;

import io.github.karlatemp.mxlib.network.IPAddress;
import io.github.karlatemp.mxlib.network.PipelineUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.naming.Context;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

/**
 * Fast helper for minecraft
 */
public class MinecraftProtocolHelper {
    private static final int DEFAULT_PROTOCOL = 498;

    public interface ListPingCallback {
        void complete(@Nullable String desc, long ms, @Nullable Throwable error);
    }

    /**
     * Parse input to real address
     *
     * @param host The input source
     * @return The real address
     */
    @NotNull
    public static IPAddress parseMinecraftServerAddress(@NotNull String host) {
        String[] var1 = host.split(":");

        if (host.startsWith("[")) {
            int var2 = host.indexOf("]");
            if (var2 > 0) {
                String var3 = host.substring(1, var2);
                String var4 = host.substring(var2 + 1).trim();
                if (var4.startsWith(":")) {
                    var4 = var4.substring(1);
                    var1 = new String[2];
                    var1[0] = var3;
                    var1[1] = var4;
                } else {
                    var1 = new String[1];
                    var1[0] = var3;
                }
            }
        }

        if (var1.length > 2) {
            var1 = new String[1];
            var1[0] = host;
        }

        String var5 = var1[0];
        int var6 = (var1.length > 1) ? parseInt(var1[1], 25565) : 25565;

        if (var6 == 25565) {
            return getMinecraftSRVAddress(var5);
        }

        return new IPAddress(var5, var6);
    }

    private static int parseInt(String var0, int var1) {
        try {
            return Integer.parseInt(var0.trim());
        } catch (Exception exception) {
            return var1;
        }
    }

    /**
     * @param host The host need resolve
     * @return SRV address
     * @since 2.12
     */
    private static IPAddress getMinecraftSRVAddress(String host) {
        try {
            // Only for load this class
            Class.forName("com.sun.jndi.dns.DnsContextFactory");

            Hashtable<String, String> var2 = new Hashtable<>();
            var2.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
            var2.put(Context.PROVIDER_URL, "dns:");
            var2.put("com.sun.jndi.dns.timeout.retries", "1");
            DirContext var3 = new InitialDirContext(var2);
            Attributes var4 = var3.getAttributes("_minecraft._tcp." + host, new String[]{"SRV"});
            String[] var5 = var4.get("srv").get().toString().split(" ", 4);
            int port = 25565;
            try {
                port = Integer.parseInt(var5[2]);
            } catch (Throwable ignore) {
            }
            return new IPAddress(var5[3], port, host, 25565);
        } catch (Throwable throwable) {
            return new IPAddress(host, 25565);
        }
    }

    public static void main(String[] args) {
        EventLoopGroup loopGroup = PipelineUtils.newEventLoopGroup();
        ListPingCallback callback = (desc, ms, err) -> {
            System.out.println("Desc = " + desc);
            System.out.println("Ms   = " + ms);
            if (err != null) {
                err.printStackTrace();
            }
            loopGroup.shutdownGracefully();
        };
        ping(new IPAddress("localhost", 25565), 498, loopGroup, callback);
    }

    public static void ping(
            IPAddress address,
            EventLoopGroup loopGroup,
            ListPingCallback callback
    ) {
        ping(address, DEFAULT_PROTOCOL, loopGroup, callback);
    }

    public static void ping(
            @NotNull IPAddress address,
            int protocol,
            @NotNull EventLoopGroup loopGroup,
            @NotNull ListPingCallback callback
    ) {
        ping(address, protocol, true, loopGroup, callback);
    }

    public static void ping(
            @NotNull IPAddress address,
            boolean checkMs,
            @NotNull EventLoopGroup loopGroup,
            @NotNull ListPingCallback callback
    ) {
        ping(address, DEFAULT_PROTOCOL, checkMs, loopGroup, callback);
    }

    /**
     * Ping a server
     *
     * @param address   The address
     * @param protocol  Protocol version
     * @param checkMs   Check delay milliseconds
     * @param loopGroup Netty EventLoopGroup
     * @param callback  The callback
     */
    public static void ping(
            @NotNull IPAddress address,
            int protocol,
            boolean checkMs,
            @NotNull EventLoopGroup loopGroup,
            @NotNull ListPingCallback callback
    ) {
        new Bootstrap()
                .group(loopGroup)
                .channel(PipelineUtils.getChannel())
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline()
                                .addLast("read-timeout", new ReadTimeoutHandler(30000, TimeUnit.MILLISECONDS))
                                .addLast("write-timeout", new WriteTimeoutHandler(30000, TimeUnit.MILLISECONDS))
                                .addLast("encoder", new MinecraftPacketMessageEncoder())
                                .addLast("decoder", new MinecraftPacketMessageDecoder())
                                .addLast("receive", new ChannelInboundHandlerAdapter() {
                                    private int step;
                                    private String desc;
                                    private long pingStartTm, pingEndTm;

                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        { // handshake
                                            PacketDataSerializer serializer = PacketDataSerializer.fromByteBuf(Unpooled.buffer(300));
                                            serializer.retain();

                                            serializer.writeVarInt(0); // id
                                            serializer.writeVarInt(protocol); // protocol
                                            serializer.writeString(address.getSourceHost()); // address host
                                            serializer.writeShort(address.getSourcePort());  // address port
                                            serializer.writeVarInt(1); // next protocol

                                            channel.writeAndFlush(serializer).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                                            serializer.release();
                                        }
                                        { // status start
                                            PacketDataSerializer serializer = PacketDataSerializer.fromByteBuf(Unpooled.buffer(2));
                                            serializer.retain();

                                            serializer.writeVarInt(0); // id

                                            channel.writeAndFlush(serializer).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                                            serializer.release();
                                        }
                                    }

                                    @Override
                                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                        long ping;
                                        if (step <= 1) {
                                            ping = -1;
                                        } else {
                                            ping = pingEndTm - pingStartTm;
                                        }
                                        callback.complete(desc, ping, cause);
                                    }

                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        if (msg instanceof ByteBuf) {
                                            PacketDataSerializer serializer = PacketDataSerializer.fromByteBuf((ByteBuf) msg);
                                            int pid = serializer.readVarInt();
                                            if (step == 0) { // Reading status
                                                if (pid != 0) {
                                                    throw new IOException("Status Response not match. Request packet id 0 but get " + pid);
                                                }
                                                desc = serializer.readString();
                                                step = 1;
                                                if (checkMs) { // ping start
                                                    PacketDataSerializer request = PacketDataSerializer.fromByteBuf(Unpooled.buffer(5));
                                                    request.retain();

                                                    request.writeVarInt(1); // id
                                                    request.writeLong(pingStartTm = System.currentTimeMillis());
                                                    ctx.channel().writeAndFlush(request).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                                                    request.release();
                                                } else {
                                                    ctx.channel().close();
                                                }
                                            } else if (step == 1) { // Ping
                                                pingEndTm = System.currentTimeMillis();
                                                ctx.channel().close();
                                                step = 2;
                                            } else { // assertion error
                                                throw new IOException("Assertion exception: step=" + step);
                                            }
                                            return;
                                        }
                                        super.channelRead(ctx, msg);
                                    }

                                    @Override
                                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                        exceptionCaught(ctx, null);
                                        super.channelInactive(ctx);
                                    }
                                })
                        ;
                    }
                })
                .connect(address.getHost(), address.getPort())
                .addListener(f -> {
                    if (!f.isSuccess()) {
                        callback.complete(null, -1, f.cause());
                    }
                })
        ;
    }
}
