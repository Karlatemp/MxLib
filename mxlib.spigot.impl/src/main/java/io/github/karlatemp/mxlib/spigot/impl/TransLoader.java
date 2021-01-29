/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-impl.main/TransLoader.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot.impl;

import io.github.karlatemp.mxlib.translate.TranslateLoader;
import io.github.karlatemp.mxlib.translate.Translator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

public class TransLoader extends TranslateLoader<JavaPlugin> {
    static final TransLoader LOADER = new TransLoader();

    @Override
    protected @Nullable Translator topTranslator() {
        return MxLibSpigotPlugin.MXLIB_TRANSLATOR;
    }

    @Override
    protected InputStream getServiceResource(JavaPlugin service, String path) throws IOException {
        return service.getResource("translates/trans" + path);
    }

    @Override
    protected InputStream getLocalResource(JavaPlugin service, String path) throws IOException {
        File file = new File(service.getDataFolder(), "translates/trans" + path);
        if (file.isFile()) {
            return new FileInputStream(file);
        }
        return null;
    }

    @Override
    protected void logError(JavaPlugin service, String msg, Throwable throwable) {
        service.getLogger().log(Level.SEVERE, msg, throwable);
    }
}
