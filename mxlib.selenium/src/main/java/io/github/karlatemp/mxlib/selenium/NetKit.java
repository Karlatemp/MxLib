package io.github.karlatemp.mxlib.selenium;

import org.apache.http.client.methods.HttpGet;

import java.io.*;
import java.util.ArrayList;

import static io.github.karlatemp.mxlib.selenium.MxSelenium.client;

class NetKit {
    interface Invoke {
        void invoke(File file, File zip) throws IOException;
    }

    static int retryCounts = 5;

    static File download(
            File file,
            String url,
            String zipname,
            Invoke unzipper
    ) throws IOException {
        return download(file, url, zipname, unzipper, false);
    }

    static File download(
            File file,
            String url,
            String zipname,
            Invoke unzipper,
            boolean override
    ) throws IOException {
        if (!file.isFile() || override) {
            File parent = file.getParentFile();
            if (parent == null) parent = new File(".");
            parent.mkdirs();
            File zip;
            if (zipname == null) zip = file;
            else zip = new File(parent, zipname);

            System.err.println("Downloading " + file.getName() + " from " + url);
            ArrayList<Throwable> errors = new ArrayList<>(retryCounts);
            for (int i = 0; i < retryCounts; i++) {
                if (i != 0) {
                    System.err.println("Download fail. retrying....");
                }
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(zip))) {
                    client.execute(new HttpGet(url)).getEntity().writeTo(out);
                } catch (Throwable throwable) {
                    //noinspection ThrowablePrintedToSystemOut
                    System.err.println(throwable);
                    errors.add(throwable);
                    continue;
                }
                if (unzipper != null)
                    unzipper.invoke(file, zip);
                return file;
            }
            IOException downloadFail = new IOException(
                    "Failed download " + file.getName() + " from " + url
            );
            for (Throwable e : errors)
                downloadFail.addSuppressed(e);
            throw downloadFail;
        }
        return file;
    }
}
