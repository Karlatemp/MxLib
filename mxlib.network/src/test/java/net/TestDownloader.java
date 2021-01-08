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
