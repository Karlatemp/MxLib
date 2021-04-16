/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-security.test/MyApplication.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package myapp;

import io.github.karlatemp.mxlib.security.MxSecurityManager;
import io.github.karlatemp.unsafeaccessor.Unsafe;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Field;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.function.Consumer;

public class MyApplication {
    private static final MxSecurityManager.Unsafe SECURITY_UNSAFE = MxSecurityManager.Unsafe.getInstance();
    static final MxSecurityManager.Unsafe.Sudo SUDO = SECURITY_UNSAFE.sudo();

    public static void main(String[] args) throws Exception {
        SECURITY_UNSAFE.trust(MyApplication.class.getProtectionDomain());
        io.github.karlatemp.unsafeaccessor.SecurityCheck.getInstance().enableSecurityCheck();
        System.setSecurityManager(new MxSecurityManager());

        ProtectionDomain NO_PERMISSION = new ProtectionDomain(null, null);
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
            try {
                System.out.println(Unsafe.getUnsafe());
                throw new AssertionError();
            } catch (SecurityException se) {
                se.printStackTrace(System.out);
            }
            return null;
        }, new AccessControlContext(new ProtectionDomain[]{NO_PERMISSION}));
        Unsafe unsafe = Unsafe.getUnsafe();
        byte[] bytecode = MyApplication.class.getResourceAsStream("MyApplication$UnTrustedCode.class").readAllBytes();
        Class<?> c = unsafe.defineClass(null, bytecode, 0, bytecode.length, MyApplication.class.getClassLoader(), NO_PERMISSION);
        Assertions.assertSame(UnTrustedCode.class, c);
        Assertions.assertSame(NO_PERMISSION, UnTrustedCode.class.getProtectionDomain());
        new UnTrustedCode().accept(new Runnable() {
            @Override
            public void run() {
                Runnable thiz = this;
                Assertions.assertSame(SUDO.run(() -> thiz.getClass().getProtectionDomain()), SUDO.run(MyApplication.class::getProtectionDomain));
                try {
                    System.out.println(Unsafe.getUnsafe());
                    throw new AssertionError();
                } catch (SecurityException se) {
                    se.printStackTrace(System.out);
                }
                try {
                    System.out.println(MxSecurityManager.Unsafe.getInstance());
                    throw new AssertionError();
                } catch (SecurityException se) {
                    se.printStackTrace(System.out);
                }
                SUDO.run(() -> System.out.println(Unsafe.getUnsafe()));
                try {
                    Field instance = MxSecurityManager.Unsafe.class.getDeclaredField("INSTANCE");
                    System.out.println(instance);
                    instance.setAccessible(true);
                    System.out.println(instance.get(null));
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        });
    }

    public static class UnTrustedCode implements Consumer<Runnable> {
        @Override
        public void accept(Runnable runnable) {
            runnable.run();
        }
    }
}
