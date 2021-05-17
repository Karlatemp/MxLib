/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.test/TestMethodOverriddenDeterminer.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package util;

import io.github.karlatemp.mxlib.utils.MethodOverriddenDeterminer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class TestMethodOverriddenDeterminer {
    @Test
    void run() throws Exception {
        MethodHandles.Lookup looker = MethodHandles.lookup();
        var det = MethodOverriddenDeterminer.of(
                looker, Object.class, "toString", MethodType.methodType(String.class)
        ).withCache();
        Assertions.assertFalse(det.isOverridden(Object.class));
        Assertions.assertTrue(det.isOverridden(String.class));
        Assertions.assertTrue(det.isOverridden(Throwable.class));
    }

    @Test
    void run2() throws Exception {
        MethodHandles.Lookup looker = MethodHandles.lookup();
        var det = MethodOverriddenDeterminer.of(
                looker, Throwable.class, "toString", MethodType.methodType(String.class)
        ).withCache();
        Assertions.assertFalse(det.isOverridden(Object.class));
        Assertions.assertFalse(det.isOverridden(String.class));
        Assertions.assertFalse(det.isOverridden(Throwable.class));
        Assertions.assertFalse(det.isOverridden(Exception.class));
        class W extends Exception {
            @Override
            public String toString() {
                return super.toString() + " {Z}";
            }
        }
        Assertions.assertTrue(det.isOverridden(W.class));
    }
}
