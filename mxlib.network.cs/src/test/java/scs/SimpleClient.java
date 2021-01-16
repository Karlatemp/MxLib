package scs;

import io.github.karlatemp.mxlib.network.PipelineUtils;
import io.github.karlatemp.mxlib.network.cs.shared.PacketDecoder;
import io.github.karlatemp.mxlib.network.cs.shared.PacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class SimpleClient {
    public static void main(String[] args) {
        EventLoopGroup loopGroup = PipelineUtils.newEventLoopGroup();
        new Bootstrap()
                .channel(PipelineUtils.getChannel())
                .group(loopGroup)
                .handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast("read-timeout", new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                                .addLast("write-timeout", new WriteTimeoutHandler(10, TimeUnit.SECONDS))
                                .addLast("packet-encoder", new PacketEncoder())
                                .addLast("packet-decoder", new PacketDecoder())
                                .addLast("message-receiver", new ChannelInboundHandlerAdapter() {

                                    @Override
                                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                        System.out.println("Channel started");
                                        ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(
                                                "Hi! SimpleServer".getBytes(StandardCharsets.UTF_8)
                                        )).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                                    }

                                    @Override
                                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                        System.out.println("Disconnected");
                                        loopGroup.shutdownGracefully();
                                    }

                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        try {
                                            System.out.println(ctx.channel() + " RESPONSE: " + msg);
                                            if (msg instanceof ByteBuf) {
                                                ByteBuf packet = (ByteBuf) msg;
                                                System.out.println(ctx.channel() + " | " + packet.toString(StandardCharsets.UTF_8));
                                                System.out.println(ctx.channel() + " | " + ByteBufUtil.hexDump(packet));
                                            }
                                        } finally {
                                            ReferenceCountUtil.release(msg);
                                        }
                                    }
                                })
                        ;
                    }

                })
                .connect("localhost", 15777)
                .addListener(future -> {
                    if (!future.isSuccess()) {
                        future.cause().printStackTrace();
                        loopGroup.shutdownGracefully();
                    }
                });
    }
}
