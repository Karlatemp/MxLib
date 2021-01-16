/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot.main/MinecraftServerHelper.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot;

import io.github.karlatemp.unsafeaccessor.Root;

import java.lang.reflect.Field;

import static io.github.karlatemp.mxlib.spigot.NmsHelper.getNmsClass;

public class MinecraftServerHelper {
    private static final Class<?> MinecraftServer = getNmsClass("MinecraftServer");
    private static final Field MS$currentTick;

    static {
        try {
            Root.openAccess(
                    MS$currentTick = MinecraftServer.getDeclaredField("currentTick")
            );
        } catch (Throwable any) {
            throw new ExceptionInInitializerError(any);
        }
    }

    public static int getCurrentTick() {
        try {
            return MS$currentTick.getInt(null);
        } catch (IllegalAccessException exception) {
            throw new InternalError(exception);
        }
    }

    public static void setCurrentTick(int tick) {
        try {
            MS$currentTick.setInt(null, tick);
        } catch (IllegalAccessException exception) {
            throw new InternalError(exception);
        }
    }
}
