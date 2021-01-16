package scs;

import io.github.karlatemp.mxlib.network.PipelineUtils;
import io.github.karlatemp.mxlib.network.cs.shared.PacketDecoder;
import io.github.karlatemp.mxlib.network.cs.shared.PacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class SimpleServer {
    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = PipelineUtils.newEventLoopGroup();
        new ServerBootstrap()
                .channel(PipelineUtils.getServerChannel())
                .group(eventLoopGroup)
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast("read-timeout", new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                                .addLast("write-timeout", new WriteTimeoutHandler(10, TimeUnit.SECONDS))
                                .addLast("packet-encoder", new PacketEncoder())
                                .addLast("packet-decoder", new PacketDecoder())
                                .addLast("message-receiver", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        try {
                                            System.out.println(ctx.channel() + " SEND: " + msg);
                                            if (msg instanceof ByteBuf) {
                                                ByteBuf packet = (ByteBuf) msg;
                                                System.out.println(ctx.channel() + " | " + packet.toString(StandardCharsets.UTF_8));
                                                System.out.println(ctx.channel() + " | " + ByteBufUtil.hexDump(packet));
                                                ctx.channel().writeAndFlush(Unpooled.wrappedBuffer("PONG!".getBytes(StandardCharsets.UTF_8)))
                                                        .addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                                            }
                                        } finally {
                                            ReferenceCountUtil.release(msg);
                                        }
                                    }
                                })
                        ;
                    }
                })
                .bind(15777)
                .addListener((ChannelFuture future) -> {
                    if (future.isSuccess()) {
                        System.out.println("Server started on " + future.channel());
                    } else {
                        future.cause().printStackTrace();
                        eventLoopGroup.shutdownGracefully();
                    }
                });
    }
}
