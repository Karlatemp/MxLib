/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/IOUtils.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.common.utils;

import io.github.karlatemp.mxlib.MxLib;
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

    // readAllBytes
    public static final ReadAllBytesInterface ReadAllBytesImpl;

    static {
        ReadAllBytesInterface def = inputStream -> {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(Math.max(BUFFER_SIZE, inputStream.available()));
            Toolkit.IO.writeTo(inputStream, bos);
            return bos.toByteArray();
        }, ret;
        try {
            ret = Reflections.findMethod(
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
            )).orElse(def);
        } catch (Throwable any) {
            ret = def;
            if(MxLib.isLoggerSetup()) {
                MxLib.getLogger().error(any);
            } else {
                any.printStackTrace();
            }
        }
        ReadAllBytesImpl = ret;
    }

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
