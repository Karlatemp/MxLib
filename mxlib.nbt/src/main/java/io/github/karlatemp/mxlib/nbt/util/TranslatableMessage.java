/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: TranslatableMessage.java@author: karlatemp@vip.qq.com: 2020/1/30 下午8:01@version: 2.0
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
