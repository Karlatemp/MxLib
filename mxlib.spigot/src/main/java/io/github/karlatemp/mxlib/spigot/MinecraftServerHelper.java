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
