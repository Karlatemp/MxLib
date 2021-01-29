/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/Trans.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.translate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.karlatemp.mxlib.format.FormatTemplate;
import io.github.karlatemp.mxlib.format.common.SimpleFormatter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"InnerClassMayBeStatic", "rawtypes"})
public class Trans {
    public static class TransByMap extends AbstractTranslator {
        private final Map map;

        public TransByMap(Map map) {
            this.map = map;
        }

        @Override
        protected @Nullable FormatTemplate findTranslateTemplate(@NotNull String key) {
            Object o = map.get(key);
            if (o != null) {
                return SimpleFormatter.INSTANCE.parse(o.toString());
            }
            return null;
        }
    }

    public static class TransByMapping extends AbstractTranslator {
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

    public static class TransByObj extends AbstractTranslator {
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

}
