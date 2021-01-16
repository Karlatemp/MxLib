/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot.main/NmsHelper.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot;

import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.unsafeaccessor.Root;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NmsHelper {
    private static final String nmsPackage;
    private static final String obcPackage;
    private static final String nmsVersion;
    private static final Class<?> CraftEntity;
    private static final MethodHandle CraftEntity$getHandle;

    private static MethodHandle mapToHandle(Object member) {
        try {
            if (member instanceof Method)
                return Root.getTrusted().unreflect((Method) member);
            if (member instanceof Constructor<?>) {
                return Root.getTrusted().unreflectConstructor((Constructor<?>) member);
            }
            throw new AssertionError();
        } catch (Exception exception) {
            return throwx(exception);
        }
    }

    private static MethodHandle mapToHandle(Field field, boolean getter) {
        try {
            if (getter)
                return Root.getTrusted().unreflectGetter(field);
            else
                return Root.getTrusted().unreflectSetter(field);
        } catch (Exception exception) {
            return throwx(exception);
        }
    }

    static {
        Class<? extends Server> klass = Bukkit.getServer().getClass();
        String klassName = klass.getName();
        // org.bukkit.craftbukkit.VERSION.CraftServer
        int lastPoint = klassName.lastIndexOf('.');
        obcPackage = klassName.substring(0, lastPoint + 1);
        int prevPoint = klassName.lastIndexOf('.', lastPoint - 1);
        nmsVersion = klassName.substring(prevPoint + 1, lastPoint);
        nmsPackage = "net.minecraft.server." + nmsVersion + '.';

        CraftEntity = getObcClass("entity.CraftEntity");
        try {
            //noinspection OptionalGetWithoutIsPresent
            CraftEntity$getHandle = Root.getTrusted().unreflect(Reflections.findMethod(CraftEntity,
                    "getHandle", false, null
            ).get());
        } catch (Exception exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    public static String getNmsPackage() {
        return nmsPackage;
    }

    public static String getNmsVersion() {
        return nmsVersion;
    }

    public static String getObcPackage() {
        return obcPackage;
    }

    public static Class<?> getObcClass(String name) {
        return findClass(obcPackage, name, null);
    }

    public static Class<?> getObcClass(String name, String... fallbacks) {
        return findClass(obcPackage, name, fallbacks);
    }// IChatBaseComponent.ChatSerializer

    public static Class<?> getNmsClass(String name) {
        return findClass(nmsPackage, name, null);
    }

    public static Class<?> getNmsClass(String name, String... fallbacks) {
        return findClass(nmsPackage, name, fallbacks);
    }

    static <T> T throwx(Throwable throwable) {
        Root.getUnsafe().throwException(throwable);
        throw new AssertionError();
    }

    private static Class<?> findClass(String pkg, String path, String[] fallbacks) {
        try {
            return Class.forName(pkg + path);
        } catch (ClassNotFoundException e) {
            if (fallbacks != null) {
                for (String f : fallbacks) {
                    try {
                        return Class.forName(pkg + f);
                    } catch (ClassNotFoundException sr) {
                        e.addSuppressed(sr);
                    }
                }
            }
            return throwx(e);
        }
    }

    public static Object getHandle(Entity entity) {
        if (entity == null) throw new NullPointerException("entity");
        try {
            return CraftEntity$getHandle.invoke(entity);
        } catch (Throwable throwable) {
            return throwx(throwable);
        }
    }
}
