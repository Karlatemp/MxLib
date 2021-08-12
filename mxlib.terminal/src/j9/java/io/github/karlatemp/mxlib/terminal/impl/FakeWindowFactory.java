/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-terminal.j9/FakeWindowFactory.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.terminal.impl;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinDef;
import io.github.karlatemp.unsafeaccessor.Root;
import io.github.karlatemp.unsafeaccessor.Unsafe;

import java.awt.*;
import java.lang.reflect.Field;

class FakeWindowFactory {
    static FakeWindowFactory factory;

    private static void setup() {
        try {
            Class.forName("com.sun.jna.Native");
        } catch (ClassNotFoundException ignore) {
            return;
        }
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            try {
                Class.forName("com.sun.jna.platform.win32.User32");
            } catch (ClassNotFoundException ignore) {
                return;
            }
            factory = new WinNtFactory();
        }
    }

    static {
        factory = new FakeWindowFactory();
        try {
            setup();
        } catch (UnsupportedOperationException ignore) {
        }
    }

    Window getConsole() {
        return null;
    }
}

class WinNtFactory extends FakeWindowFactory {
    final Class<?> WWindowPeer;
    final Field WComponentPeer$hwnd;
    final Field Component$peer;
    final Window window;

    WinNtFactory() {
        try {
            WWindowPeer = Class.forName("sun.awt.windows.WWindowPeer");
            WComponentPeer$hwnd = Root.openAccess(
                    Class.forName("sun.awt.windows.WComponentPeer").getDeclaredField("hwnd")
            );
            Component$peer = Root.openAccess(
                    Component.class.getDeclaredField("peer")
            );
            WinDef.HWND hwnd = Kernel32.INSTANCE.GetConsoleWindow();
            if (hwnd == null || hwnd.getPointer() == null) {
                window = null;
            } else {
                Unsafe usf = Unsafe.getUnsafe();
                window = (Window) usf.allocateInstance(Window.class);
                Object peer = usf.allocateInstance(WWindowPeer);
                WComponentPeer$hwnd.setLong(peer, Pointer.nativeValue(hwnd.getPointer()));
                Component$peer.set(window, peer);
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    Window getConsole() {
        return window;
    }
}
