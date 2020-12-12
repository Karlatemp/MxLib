package io.github.karlatemp.mxlib.common.utils;

import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.mxlib.utils.Toolkit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class IOUtils {
    private static final MethodHandles.Lookup lk = MethodHandles.lookup();
    private static final int BUFFER_SIZE = 1024 * 10;

    interface ReadAllBytesInterface {
        byte[] readAll(InputStream is) throws IOException;
    }

    // dropClassLoaderName
    public static final ReadAllBytesInterface ReadAllBytesImpl = Reflections.findMethod(
            InputStream.class,
            "readAllBytes",
            false,
            byte[].class
    ).map(method -> Reflections.bindToNoErr(
            lk,
            Reflections.mapToHandle(method),
            ReadAllBytesInterface.class,
            MethodType.methodType(byte[].class, InputStream.class),
            "readAll"
    )).orElse(inputStream -> {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(Math.max(BUFFER_SIZE, inputStream.available()));
        Toolkit.IO.writeTo(inputStream, bos);
        return bos.toByteArray();
    });

    public static byte[] readAllBytes(InputStream is) throws IOException {
        return ReadAllBytesImpl.readAll(is);
    }

    public static byte[] readAllBytesNoErr(InputStream is) {
        try {
            return ReadAllBytesImpl.readAll(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
