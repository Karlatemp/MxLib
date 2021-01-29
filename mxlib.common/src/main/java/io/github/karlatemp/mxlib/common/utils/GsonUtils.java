/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/GsonUtils.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.common.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.Reader;
import java.util.function.Function;

@SuppressWarnings("deprecation")
public class GsonUtils {
    public static final Function<Reader, JsonElement> PARSE_READER = new Object() {
        Function<Reader, JsonElement> a() {
            try {
                JsonParser.class.getDeclaredMethod("parseReader", Reader.class);
                return JsonParser::parseReader;
            } catch (Throwable ignored) {
                JsonParser parser = new JsonParser();
                return parser::parse;
            }
        }
    }.a();
}
