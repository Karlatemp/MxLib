package io.github.karlatemp.mxlib.spigot.impl;

import io.github.karlatemp.mxlib.format.FormatTemplate;
import io.github.karlatemp.mxlib.format.common.SimpleFormatter;
import io.github.karlatemp.mxlib.translate.AbstractTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class TransByMapping extends AbstractTranslator {
    private final Function<String, String> mapper;

    public TransByMapping(Function<String, String> mapper) {
        this.mapper = mapper;
    }

    @Override
    protected @Nullable FormatTemplate findTranslateTemplate(@NotNull String key) {
        String o = mapper.apply(key);
        if (o != null) {
            return SimpleFormatter.INSTANCE.parse(o);
        }
        return null;
    }
}
