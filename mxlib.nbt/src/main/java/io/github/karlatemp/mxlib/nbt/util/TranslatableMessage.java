/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-nbt.main/TranslatableMessage.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.nbt.util;

import com.mojang.brigadier.Message;
import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.translate.SystemTranslator;
import io.github.karlatemp.mxlib.translate.Translator;

public class TranslatableMessage implements Message {
    public static final ThreadLocal<Translator> TRANSLATOR = new ThreadLocal<>();
    private final String message;
    private final Object[] params;
    private transient Translator translator;

    public Translator getTranslator() {
        return translator;
    }

    public void setTranslate(Translator translator) {
        this.translator = translator;
    }

    public TranslatableMessage(String message, Object... params) {
        this.message = message;
        this.params = params;
    }

    @Override
    public String getString() {
        return getMessage();
    }

    public String getMessage() {
        if (translator != null)
            return translator.format(message, params).to_string();
        final IBeanManager manager = MxLib.getBeanManager();
        final Translator translator = TRANSLATOR.get();
        if (translator != null) return translator.format(message, params).to_string();
        final SystemTranslator systemTranslate = manager.getBy(SystemTranslator.class).orElse(null);
        if (systemTranslate != null)
            return systemTranslate.format(message, params).to_string();
        return message;
    }
}
