/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/AbstractTranslator.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.translate;

import io.github.karlatemp.mxlib.format.FormatArguments;
import io.github.karlatemp.mxlib.format.FormatTemplate;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractTranslator implements Translator {
    protected static class TranslateNotFound implements FormatTemplate {
        private final String key;

        public TranslateNotFound(String key) {
            this.key = key;
        }

        @Override
        public void formatTo(@NotNull StringBuilder buffer, @NotNull FormatArguments arguments) {
            buffer.append("${").append(key);
            int size = arguments.arguments();
            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    buffer.append(", ");
                    StringBuilderFormattable.append(buffer, arguments.get(i));
                }
            }
            buffer.append('}');
        }
    }

    public static class Link extends AbstractTranslator {

        private final Iterable<AbstractTranslator> translators;

        public Link(Iterable<AbstractTranslator> translators, Translator parent) {
            this.translators = translators;
            this.parent = parent;
        }

        @Override
        protected @Nullable FormatTemplate findTranslateTemplate(@NotNull String key) {
            for (AbstractTranslator translator : translators) {
                FormatTemplate template = translator.findTranslateTemplate(key);
                if (template != null) return template;
            }
            return null;
        }
    }

    public static final class Empty extends AbstractTranslator {
        public static final Empty INSTANCE = new Empty();
        @Override
        protected @Nullable FormatTemplate findTranslateTemplate(@NotNull String key) {
            return null;
        }
    }

    protected Translator parent;

    public AbstractTranslator() {
    }

    public AbstractTranslator(Translator parent) {
        this.parent = parent;
    }

    @Override
    public @NotNull FormatTemplate getTranslateTemplate(@NotNull String key) {
        FormatTemplate result = findTranslateTemplate(key);
        if (result != null) return result;
        if (parent != null) return parent.getTranslateTemplate(key);
        return new TranslateNotFound(key);
    }

    protected abstract @Nullable FormatTemplate findTranslateTemplate(@NotNull String key);

    @Override
    public @NotNull StringBuilderFormattable format(@NotNull String key) {
        return getTranslateTemplate(key).format(FormatArguments.EMPTY);
    }

    @Override
    public @NotNull StringBuilderFormattable format(@NotNull String key, Object... arguments) {
        return getTranslateTemplate(key).format(FormatArguments.by(arguments));
    }
}
