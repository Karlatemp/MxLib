/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.main/CommandProvider.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.command;

import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.translate.Translator;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The commands provider
 */
public interface CommandProvider {
    /**
     * Create new command in a package.
     *
     * @param package_ Command Class package.
     * @param classes  Class found.
     * @return The build-in command.
     */
    io.github.karlatemp.mxlib.command.ICommand buildCommands(Package package_, List<Class<?>> classes);

    /**
     * Build command in a class
     *
     * @param commandClass The source of command.
     * @return A command build.
     */
    @Nullable
    io.github.karlatemp.mxlib.command.ICommand buildCommand(Class<?> commandClass);

    /**
     * Try resolve sender. null if fail.
     *
     * @param sender  The sender object.
     * @param toClass Target class check(use in command). null if don't check type.
     * @return null if fail. or resolved object.
     * @see io.github.karlatemp.mxlib.command.annoations.MSender
     * @see io.github.karlatemp.mxlib.command.DefaultCommand#check(Object)
     */
    @Nullable
    Object resolveSender(Object sender, @Nullable Class<?> toClass);

    /**
     * Call when sender cannot resolve to target class.
     *
     * @param sender  The resolved object.
     * @param toClass Target class.
     */
    void senderNotResolve(Object sender, Class<?> toClass);

    /**
     * Call when command class used current class.
     *
     * @param provider The parent provider.
     * @return A Provider with parent. Normally it should return a new instance.
     */
    @NotNull
    CommandProvider withParent(CommandProvider provider);

    /**
     * Check sender has permission or not.
     *
     * @param sender     The sender need check
     * @param permission The permission need check.
     * @return The sender has permission or not.
     */
    boolean hasPermission(Object sender, String permission);

    /**
     * Executed when denied for lack of permissions
     *
     * @param sender  The sender.
     * @param command The denied command.
     */
    void noPermission(Object sender, io.github.karlatemp.mxlib.command.ICommand command);

    /**
     * Send a message to sender.
     *
     * @param level   The message level.
     * @param sender  The sender.
     * @param message The message.
     */
    default void sendMessage(Level level, Object sender, String message) {
        sendMessage(level, sender, StringBuilderFormattable.by(message));
    }

    void sendMessage(Level level, Object sender, StringBuilderFormattable message);

    default void sendMessage(Level level, Object sender, Object message) {
        sendMessage(level, sender, StringBuilderFormattable.by(String.valueOf(message)));
    }

    /**
     * Try send a translate message to sender.
     *
     * @param level  The sending level.
     * @param sender The sender.
     * @param trans  The translate.
     */
    void translate(Level level, Object sender, String trans);

    /**
     * Try send a translate message to sender.
     *
     * @param level  The sending level.
     * @param sender The sender.
     * @param trans  The translate.
     * @param params The translate parameters.
     */
    void translate(Level level, Object sender, String trans, Object... params);

    /**
     * Get help template.
     *
     * @return The template using.
     */
    HelpTemplate getHelp();

    /**
     * Get Provider's Logger
     */
    Logger logger();

    Translator translator();

    default String parse_message(String desc) {
        return desc;
    }

    void setBeanManager(IBeanManager beanManager);

    IBeanManager getBeanManager();
}
