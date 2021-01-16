/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network.test/TestDownloader.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package net;

import io.github.karlatemp.mxlib.network.FileDownloader;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.util.concurrent.Executors;

public class TestDownloader {
    public static void main(String[] args) throws Throwable {
        FileDownloader.download(
                new File("build/test.jpg"),
                HttpClientBuilder.create()
                        .build(),
                "https://i0.hdslb.com/bfs/archive/36b74576fd34bbd889051e2d03dc2d35f7ddb258.jpg",
                Executors.newCachedThreadPool(t -> {
                    Thread th = new Thread(t);
                    th.setDaemon(true);
                    return th;
                }),
                3,
                0,
                5
        );
    }
}
