/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-security.main/MxSecurityManager.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.security;

import io.github.karlatemp.mxlib.utils.Toolkit;
import org.jetbrains.annotations.NotNull;

import java.security.*;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

public class MxSecurityManager extends SecurityManager {
    static final ProtectionDomain SUDO = new ProtectionDomain(null, new Object() {
        PermissionCollection a() {
            AllPermission allPermission = new AllPermission();
            PermissionCollection collection = allPermission.newPermissionCollection();
            collection.add(allPermission);
            return collection;
        }
    }.a(), null, null) {
        @Override
        public String toString() {
            return "ProtectionDomain(SUDO)";
        }
    };
    static final Set<String> NON_EXPORTED_PACKAGES = new HashSet<>();

    // private static final AccessControlContext SUDO_CONTEXT = new AccessControlContext(new ProtectionDomain[]{SUDO});
    private static final Permission PERMISSION_GET_UNSAFE = new RuntimePermission("mxsecurity.getUnsafe");

    public static class Unsafe {
        public interface Sudo {
            <V> V run(Supplier<V> action);

            void run(Runnable action);

            <V> V call(Callable<V> action) throws Exception;

            <T, R> R run(Function<T, R> action, T arg);
        }

        private static boolean firstTime = true;

        public static Unsafe getInstance() {
            if (firstTime) {
                synchronized (Unsafe.class) {
                    if (firstTime) {
                        firstTime = false;
                        return INSTANCE;
                    }
                }
            }
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkPermission(PERMISSION_GET_UNSAFE);
            }
            return INSTANCE;
        }

        @SuppressWarnings("FieldMayBeFinal")
        private volatile boolean trusted;

        private Unsafe() {
            if (INSTANCE != null) throw new IllegalStateException("INSTANCE initialized");
            trusted = true;
        }

        static final Unsafe INSTANCE = new Unsafe();

        private void checkInit() {
            if (!trusted) {
                throw new IllegalStateException("not initialized");
            }
        }

        public void addNonExportedPackage(String pkg) {
            checkInit();
            NON_EXPORTED_PACKAGES.add(pkg);
        }

        public void removeNonExportedPackage(String pkg) {
            checkInit();
            NON_EXPORTED_PACKAGES.add(pkg);
        }

        public @NotNull Sudo sudo() {
            checkInit();
            return Kit.sudo;
        }

        public void trust(ProtectionDomain domain) {
            checkInit();
            Kit.trust(domain);
        }

        public ProtectionDomain getSudoProtectionDomain() {
            checkInit();
            return SUDO;
        }
    }

    static {
        Kit.setup();
        NON_EXPORTED_PACKAGES.add(Toolkit.getPackageByClassName(MxSecurityManager.class.getName()));
    }

    @Override
    public void checkPermission(Permission perm) {
        AccessControlContext context = Kit.getStackAccessControlContext();

        if (context != null) {
            ProtectionDomain[] protectionDomains = Kit.getContext(AccessController.getContext());
            if (protectionDomains != null) {
                for (ProtectionDomain p : protectionDomains) {
                    if (p == SUDO)
                        return;
                }
            }
        }
        // getContext
        super.checkPermission(perm);
    }

    @Override
    public void checkPackageAccess(String pkg) {
        super.checkPackageAccess(pkg);
        if (NON_EXPORTED_PACKAGES.contains(pkg)) {
            checkPermission(
                    new RuntimePermission("accessClassInPackage." + pkg));
        }
    }
}
