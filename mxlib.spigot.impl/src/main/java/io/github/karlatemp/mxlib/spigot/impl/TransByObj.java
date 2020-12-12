package io.github.karlatemp.mxlib.spigot.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.karlatemp.mxlib.format.FormatTemplate;
import io.github.karlatemp.mxlib.format.common.SimpleFormatter;
import io.github.karlatemp.mxlib.translate.AbstractTranslator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TransByObj extends AbstractTranslator {
    private final JsonObject obj;

    public TransByObj(JsonObject object) {
        this.obj = object;
    }

    @Override
    protected @Nullable FormatTemplate findTranslateTemplate(@NotNull String key) {
        JsonElement element = obj.get(key);
        if (element != null) {
            return SimpleFormatter.INSTANCE.parse(element.getAsString());
        }
        return null;
    }
}
