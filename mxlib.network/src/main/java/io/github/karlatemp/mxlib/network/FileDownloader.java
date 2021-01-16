/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network.main/FileDownloader.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.network;

import io.github.karlatemp.mxlib.utils.RafOutputStream;
import io.github.karlatemp.mxlib.utils.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class FileDownloader {
    public static void download(
            File target,
            HttpClient client,
            String uri,
            Executor executor,
            int threads,
            long downloadRangeSize,
            int retryCount
    ) throws IOException {
        HttpResponse request = client.execute(RequestBuilder.get()
                .setUri(uri)
                .addHeader("Range", "bytes=0-")
                .build()
        );
        int statusCode = request.getStatusLine().getStatusCode();
        if (statusCode != 200 && statusCode != 206) {
            throw new IOException("Except 200 to download but get " + request.getStatusLine());
        }
        long downloadRangeSize0;
        if (downloadRangeSize < 1) {
            downloadRangeSize0 = 1024L * 1024 * 50;
        } else {
            downloadRangeSize0 = downloadRangeSize;
        }

        Header rangeResponseHeader = request.getFirstHeader("Content-Range");
        if (rangeResponseHeader == null) {
            try (OutputStream rafOutputStream = new BufferedOutputStream(new RafOutputStream(
                    new RandomAccessFile(target, "rw")
            ))) {
                request.getEntity().writeTo(rafOutputStream);
            }
            return;
        }
        request.getEntity().getContent().close();
        String range = rangeResponseHeader.getValue();
        long size = Long.parseLong(StringUtils.substringAfter(range, "/", ""));
        try (RandomAccessFile raf = new RandomAccessFile(target, "rw")) {
            raf.setLength(size);
        }
        AtomicLong rangeCounter = new AtomicLong(0);
        List<CompletableFuture<Void>> fs = new ArrayList<>(threads);
        AtomicBoolean run = new AtomicBoolean(true);
        while (threads-- > 0) {
            CompletableFuture<Void> f = new CompletableFuture<>();
            fs.add(f);
            executor.execute(() -> {
                try {
                    download(rangeCounter, target, client, uri, size, downloadRangeSize0, retryCount, run);
                    f.complete(null);
                } catch (IOException exception) {
                    run.set(false);
                    f.completeExceptionally(exception);
                }
            });
        }
        Throwable throwable = null;
        for (CompletableFuture<Void> fv : fs) {
            try {
                try {
                    fv.get();
                } catch (ExecutionException e) {
                    throw e.getCause();
                }
            } catch (Throwable tx) {
                if (throwable == null) throwable = tx;
                else throwable.addSuppressed(tx);
            }
        }
        thrIOE(throwable);
    }

    private static void thrIOE(Throwable throwable) throws IOException {
        if (throwable instanceof IOException) {
            throw (IOException) throwable;
        } else if (throwable instanceof RuntimeException) {
            throw (RuntimeException) throwable;
        } else if (throwable instanceof Error) {
            throw (Error) throwable;
        } else if (throwable != null) {
            throw new IOException(throwable);
        }
    }

    private static void download(
            AtomicLong rangeCounter,
            File file,
            HttpClient client,
            String uri,
            long size,
            long downloadRangeSize,
            int retryCount,
            AtomicBoolean run
    ) throws IOException {
        retryCount = Math.max(1, retryCount);
        while (run.get()) {
            // try get range
            long start, end;
            while (true) {
                long val = rangeCounter.get();
                if (val >= size) return; // finished
                long rangeEnd = Math.min(val + downloadRangeSize, size);
                if (rangeCounter.compareAndSet(val, rangeEnd)) {
                    start = val;
                    end = rangeEnd;
                    break;
                }
            }
            int rcrc = retryCount;
            Throwable ttr = null;
            while (run.get() && rcrc-- > 0) {
                try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                    raf.seek(start);
                    HttpResponse request = client.execute(RequestBuilder.get()
                            .setUri(uri)
                            .addHeader("Range", "bytes=" + start + "-" + (end - 1))
                            .build()
                    );
                    if (request.getStatusLine().getStatusCode() != 206) {
                        throw new IOException("Except 206 to download but get " + request.getStatusLine());
                    }
                    try (OutputStream o = new BufferedOutputStream(
                            new RafOutputStream(raf, false, false)
                    )) {
                        request.getEntity().writeTo(o);
                    }
                } catch (Throwable throwable) {
                    if (ttr == null) ttr = throwable;
                    else ttr.addSuppressed(throwable);
                }
            }
            thrIOE(ttr);
        }
    }
}
