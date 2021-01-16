/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network.main/PipelineUtils.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.network;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.PlatformDependent;

import java.util.concurrent.ThreadFactory;

public class PipelineUtils {
    private static final boolean epoll;

    static {
        if (PlatformDependent.isWindows()) {
            epoll = false;
        } else {
            epoll = Epoll.isAvailable();
        }
    }

    public static Class<? extends Channel> getChannel() {
        return epoll ? io.netty.channel.epoll.EpollSocketChannel.class : io.netty.channel.socket.nio.NioSocketChannel.class;
    }

    public static Class<? extends Channel> getDatagramChannel() {
        return epoll ? io.netty.channel.epoll.EpollDatagramChannel.class : io.netty.channel.socket.nio.NioDatagramChannel.class;
    }

    public static Class<? extends ServerChannel> getServerChannel() {
        return epoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }

    public static EventLoopGroup newEventLoopGroup() {
        return epoll ? new EpollEventLoopGroup() : new NioEventLoopGroup();
    }

    public static EventLoopGroup newEventLoopGroup(int threads) {
        return epoll ? new EpollEventLoopGroup(threads) : new NioEventLoopGroup(threads);
    }

    public static EventLoopGroup newEventLoopGroup(int threads, ThreadFactory factory) {
        return epoll ? new EpollEventLoopGroup(threads, factory) : new NioEventLoopGroup(threads, factory);
    }
}
