/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger-slf4j.main/MSlf4JLoggerInstaller.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.slf4j;

import io.github.karlatemp.unsafeaccessor.Unsafe;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;
import org.slf4j.helpers.NOPServiceProvider;

import java.lang.reflect.Field;

public class MSlf4JLoggerInstaller {
    static final Unsafe usf = Unsafe.getUnsafe();

    static final int UNINITIALIZED = 0;
    static final int ONGOING_INITILIZATION = 1;
    static final int FAILED_INITILIZATION = 2;
    static final int SUCCESSFUL_INITILIZATION = 3;
    static final int NOP_FALLBACK_INITILIZATION = 4;

    static int INITIALIZATION_STATE = UNINITIALIZED;


    public static void install(ILoggerFactory factory) throws Exception {
        Class<LoggerFactory> target = LoggerFactory.class;
        NOPLoggerFactory delegate = new NOPLoggerFactory() {
            @Override
            public Logger getLogger(String name) {
                return factory.getLogger(name);
            }
        };
        usf.ensureClassInitialized(target);
        Field initializationStateField = target.getDeclaredField("INITIALIZATION_STATE");
        Field nopFallbackFactoryField = target.getDeclaredField("NOP_FALLBACK_FACTORY");
        usf.putInt(
                usf.staticFieldBase(initializationStateField),
                usf.staticFieldOffset(initializationStateField),
                NOP_FALLBACK_INITILIZATION
        );
        try {
            usf.putReference(
                    usf.staticFieldBase(nopFallbackFactoryField),
                    usf.staticFieldOffset(nopFallbackFactoryField),
                    new NOPServiceProvider() {
                        @Override
                        public ILoggerFactory getLoggerFactory() {
                            return delegate;
                        }
                    }
            );
        } catch (Throwable ignored) {
            usf.putReference(
                    usf.staticFieldBase(nopFallbackFactoryField),
                    usf.staticFieldOffset(nopFallbackFactoryField),
                    delegate
            );
        }
    }
}
