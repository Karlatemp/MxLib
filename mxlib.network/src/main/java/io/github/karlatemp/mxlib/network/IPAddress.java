/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network.main/IPAddress.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
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
