/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/BaseCommandProvider.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.command;

import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.mxlib.translate.Translator;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BaseCommandProvider extends AbstractCommandProvider {
    protected CommandProvider parent;
    protected Translator translator;
    protected Logger logger;
    protected HelpTemplate helpTemplate;
    protected IBeanManager beanManager;


    public BaseCommandProvider(CommandProvider parent, Translator translator) {
        this(parent, translator, Logger.getGlobal());
    }

    public BaseCommandProvider(CommandProvider parent, Translator translator, Logger logger) {
        this.parent = parent;
        this.translator = translator;
        this.logger = logger;
    }

    public BaseCommandProvider(CommandProvider parent) {
        this(parent, null);
    }

    public BaseCommandProvider() {
        this(null, null);
    }

    @Override
    public Object resolveSender(Object sender, Class<?> toClass) {
        if (parent == null) {
            if (toClass == null) return sender;
            if (toClass.isInstance(sender)) return sender;
            return null;
        }
        return parent.resolveSender(sender, toClass);
    }

    @Override
    public void senderNotResolve(Object sender, Class<?> toClass) {
        if (parent != null)
            parent.senderNotResolve(sender, toClass);
    }

    @NotNull
    @Override
    public CommandProvider withParent(CommandProvider provider) {
        return Reflections.allocObject(getClass()).copy(provider);
    }

    @Override
    public boolean hasPermission(Object sender, String permission) {
        if (parent == null) {
            return permission == null || permission.trim().isEmpty();
        }
        return parent.hasPermission(sender, permission);
    }

    @Override
    public void noPermission(Object sender, io.github.karlatemp.mxlib.command.ICommand command) {
        if (parent != null) parent.noPermission(sender, command);
    }

    @Override
    public void sendMessage(Level level, Object sender, String message) {
        if (parent != null) parent.sendMessage(level, sender, message);
    }

    @Override
    public void sendMessage(Level level, Object sender, StringBuilderFormattable message) {
        if (parent != null) parent.sendMessage(level, sender, message);
    }

    @Override
    public void translate(Level level, Object sender, String trans) {
        if (translator != null) {
            sendMessage(level, sender, translator.format(trans));
        } else if (parent != null) {
            parent.translate(level, sender, trans);
        }
    }

    @Override
    public void translate(Level level, Object sender, String trans, Object... params) {
        if (translator != null) {
            sendMessage(level, sender, translator.format(trans, params));
        } else if (parent != null) {
            parent.translate(level, sender, trans, params);
        }
    }

    protected BaseCommandProvider copy(CommandProvider provider) {
        this.parent = provider;
        this.translator = provider.translator();
        this.logger = provider.logger();
        this.helpTemplate = provider.getHelp();
        return this;
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public Translator translator() {
        return translator;
    }

    @Override
    public HelpTemplate getHelp() {
        if (helpTemplate != null) return helpTemplate;
        if (parent != null) return parent.getHelp();
        return helpTemplate = new HelpTemplate();
    }

    @Override
    public IBeanManager getBeanManager() {
        if (beanManager != null) return beanManager;
        if (parent != null) return parent.getBeanManager();
        return null;
    }

    @Override
    public void setBeanManager(IBeanManager beanManager) {
        this.beanManager = beanManager;
    }
}
