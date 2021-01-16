/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/CacheCommandProviders.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.command;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.karlatemp.mxlib.reflect.Reflections;

import java.util.concurrent.TimeUnit;

public class CacheCommandProviders {
    private static final Cache<Class<?>, CommandProvider> PROVIDERS = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.MILLISECONDS).build();

    public static CommandProvider getProvider(Class<? extends CommandProvider> provider) {
        CommandProvider p = PROVIDERS.getIfPresent(provider);
        if (p != null) return p;
        try {
            CommandProvider provider1 = Reflections.allocObject(provider);
            PROVIDERS.put(provider, provider1);
            return provider1;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
