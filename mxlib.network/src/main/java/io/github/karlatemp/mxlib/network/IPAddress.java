/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: IPAddress.java@author: karlatemp@vip.qq.com: 2020/1/14 上午1:56@version: 2.0
 */

package io.github.karlatemp.mxlib.network;

import java.net.IDN;

public class IPAddress {
    private final int port;
    private final String host;
    private final int sourcePort;
    private final String sourceHost;

    public IPAddress(String host, int port) {
        this(host, port, host, port);
    }

    public IPAddress(String host, int port, String sourceHost, int sourcePort) {
        this.host = host;
        this.port = port;
        this.sourceHost = sourceHost;
        this.sourcePort = sourcePort;
    }

    public int getPort() {
        return port;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public String getSourceHost() {
        return sourceHost;
    }

    public String getHost() {
        try {
            return IDN.toASCII(host);
        } catch (IllegalArgumentException err) {
            return "";
        }
    }

    @Override
    public String toString() {
        return "IPAddress{" +
                "port=" + port +
                ", host='" + host + '\'' +
                ", sourcePort=" + sourcePort +
                ", sourceHost='" + sourceHost + '\'' +
                '}';
    }
}
