/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/TranslateLoader.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.translate;

import com.google.gson.JsonObject;
import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.common.utils.GsonUtils;
import io.github.karlatemp.mxlib.logger.MLogger;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;

public abstract class TranslateLoader<T> {
    protected interface RsType {
        <T> InputStream getResource(TranslateLoader<T> loader, T service, String path) throws IOException;
    }

    protected static final RsType SERVICE = TranslateLoader::getServiceResource;
    protected static final RsType LOCAL = TranslateLoader::getLocalResource;

    protected abstract @Nullable Translator topTranslator();

    protected abstract InputStream getServiceResource(T service, String path) throws IOException;

    protected abstract InputStream getLocalResource(T service, String path) throws IOException;

    protected Locale getLocale(T service) {
        return Locale.getDefault();
    }

    protected List<Locale> locales(T service) {
        return ResourceBundle.Control
                .getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT)
                .getCandidateLocales("", Locale.getDefault());
    }

    protected MLogger logger() {
        return MxLib.getLoggerOrNop();
    }

    protected abstract void logError(T service, String msg, Throwable throwable);

    protected String localeKey(Locale locale) {
        String string = locale.toString();
        if (string.isEmpty()) return string;
        return "_" + string;
    }

    protected Reader toReader(InputStream stream) {
        if (stream == null) return null;
        return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    public Translator loadTranslate(T service) {
        List<Locale> locales = locales(service);

        List<AbstractTranslator> translators = new ArrayList<>(locales.size());
        MLogger logger = logger();
        logger.debug(StringBuilderFormattable.by(() -> "Loading translates for " + service));
        for (Locale locale : locales) {
            String key = localeKey(locale);
            logger.debug(StringBuilderFormattable.by(() -> "Loading `" + locale + "` with key `" + key + "`"));
            loadTranslate(translators, logger, locale, key, service, LOCAL);
            loadTranslate(translators, logger, locale, key, service, SERVICE);
        }
        return new AbstractTranslator.Link(translators, topTranslator());
    }

    protected void loadTranslate(
            List<AbstractTranslator> translators,
            MLogger logger,
            Locale locale, String key, T service, RsType type
    ) {
        try (Reader reader = toReader(type.getResource(this, service, key + ".json"))) {
            if (reader != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Json found. Loading with key `" + key + "`");
                }
                JsonObject obj = GsonUtils.PARSE_READER.apply(reader).getAsJsonObject();
                translators.add(new Trans.TransByObj(obj));
            }
        } catch (Exception exception) {
            logError(service, "Error in reading translate with json", exception);
        }
        try (Reader reader = toReader(type.getResource(this, service, key + ".properties"))) {
            if (reader != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Properties found. Loading with key `" + key + "`");
                }

                Properties properties = new Properties();
                properties.load(reader);
                translators.add(new Trans.TransByMap(properties));
            }
        } catch (Exception exception) {
            logError(service, "Error in reading translate with properties", exception);
        }
    }

    public static class WithClassLoader extends TranslateLoader<Void> {
        protected final Translator top;
        protected final ClassLoader loader;
        private final String prefix;
        private final BiConsumer<String, Throwable> errorRecorder;

        public WithClassLoader(
                @NotNull ClassLoader loader,
                @NotNull String prefix,
                @NotNull BiConsumer<String, Throwable> errorRecorder
        ) {
            this(null, loader, prefix, errorRecorder);
        }

        public WithClassLoader(
                @Nullable Translator top,
                @NotNull ClassLoader loader,
                @NotNull String prefix,
                @NotNull BiConsumer<String, Throwable> errorRecorder
        ) {
            this.top = top;
            this.loader = loader;
            this.prefix = prefix;
            this.errorRecorder = errorRecorder;
        }

        @Override
        protected @Nullable Translator topTranslator() {
            return top;
        }

        @Override
        protected InputStream getServiceResource(Void service, String path) {
            return loader.getResourceAsStream(prefix + path);
        }

        @Override
        protected InputStream getLocalResource(Void service, String path) {
            return null;
        }

        @Override
        protected void logError(Void service, String msg, Throwable throwable) {
            errorRecorder.accept(msg, throwable);
        }
    }
}
