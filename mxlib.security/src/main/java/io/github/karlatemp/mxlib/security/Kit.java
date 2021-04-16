/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-security.main/Kit.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.security;

import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.reflect.LambdaFactory;
import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.mxlib.reflect.WrappedClassImplements;
import io.github.karlatemp.unsafeaccessor.Root;
import io.github.karlatemp.unsafeaccessor.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.ProtectionDomain;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
class Kit {
    static final Function<AccessControlContext, ProtectionDomain[]> getContext;
    static final Field hasAllPermissions;
    static final Supplier<AccessControlContext> getStackAccessControlContext;
    static final Class<? extends MxSecurityManager.Unsafe.Sudo> wrapperclass;
    static final MxSecurityManager.Unsafe.Sudo sudo;

    static void trust(ProtectionDomain domain) {
        if (domain != null) {
            try {
                hasAllPermissions.setBoolean(domain, true);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    static ProtectionDomain[] getContext(AccessControlContext context) {
        if (context == null) return null;
        return getContext.apply(context);
    }

    static AccessControlContext getStackAccessControlContext() {
        return getStackAccessControlContext.get();
    }

    static {
        try {
            Unsafe unsafe = Root.getUnsafe();
            hasAllPermissions = Root.openAccess(ProtectionDomain.class.getDeclaredField("hasAllPerm"));
            MethodHandles.Lookup trusted = Root.getTrusted();

            getContext = LambdaFactory.bridge(
                    Function.class,
                    trusted.in(AccessControlContext.class),
                    trusted.findVirtual(AccessControlContext.class, "getContext", MethodType.methodType(ProtectionDomain[].class))
            );
            getStackAccessControlContext = LambdaFactory.bridge(
                    Supplier.class,
                    trusted.in(AccessController.class),
                    trusted.findStatic(AccessController.class, "getStackAccessControlContext", MethodType.methodType(AccessControlContext.class))
            );
            trust(Kit.class.getProtectionDomain());
            trust(MxSecurityManager.class.getProtectionDomain());
            trust(Reflections.class.getProtectionDomain());
            trust(Unsafe.class.getProtectionDomain());
            trust(MxLib.class.getProtectionDomain());

            byte[] code = WrappedClassImplements.genDelegate(null, MxSecurityManager.Unsafe.Sudo.class).toByteArray();
            wrapperclass = unsafe.defineClass(null, code, 0, code.length, MxSecurityManager.Unsafe.Sudo.class.getClassLoader(), MxSecurityManager.SUDO)
                    .asSubclass(MxSecurityManager.Unsafe.Sudo.class);
            sudo = Root.openAccess(wrapperclass.getDeclaredConstructor(MxSecurityManager.Unsafe.Sudo.class))
                    .newInstance(new MxSecurityManager.Unsafe.Sudo() {
                        @Override
                        public <T> T run(Supplier<T> action) {
                            return action.get();
                        }

                        @Override
                        public void run(Runnable action) {
                            action.run();
                        }

                        @Override
                        public <T, R> R run(Function<T, R> action, T arg) {
                            return action.apply(arg);
                        }

                        @Override
                        public <V> V call(Callable<V> callable) throws Exception {
                            return callable.call();
                        }
                    });

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    static void setup() {
    }
}
