/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-command.test/Example0.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package ex0;

import ex0.cmd.Cmd1;
import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.command.CommandBuilder;
import io.github.karlatemp.mxlib.command.ICommand;
import io.github.karlatemp.mxlib.command.PrintStreamProvider;
import io.github.karlatemp.mxlib.common.utils.BeanManagers;
import io.github.karlatemp.mxlib.logger.AwesomeLogger;
import io.github.karlatemp.mxlib.logger.BasicMessageFactory;
import io.github.karlatemp.mxlib.logger.renders.SimpleRender;
import io.github.karlatemp.mxlib.translate.TranslateLoader;
import io.github.karlatemp.mxlib.translate.Translator;

import java.util.List;

public class Example0 {
    public static void main(String[] args) {
        // setup debugging logger
        MxLib.setLogger(new AwesomeLogger.Awesome(
                "MxLib", System.out::println,
                new SimpleRender(new BasicMessageFactory())
        ));

        // Load MxLib command translates
        Translator translator = new TranslateLoader.WithClassLoader(
                Example0.class.getClassLoader(),
                "mxlib-command-translates/trans",
                (m, s) -> {
                    System.err.println(m);
                    s.printStackTrace();
                }
        ).loadTranslate(null);

        PrintStreamProvider provider = new PrintStreamProvider(null, translator);
        // if MxLib.beanManager not initialized. Must setup bean manager
        provider.setBeanManager(BeanManagers.newStandardManager());
        ICommand command = new CommandBuilder()
                .provider(provider)
                .ofClass(Cmd1.class) // Any class in package `ex0.cmd`
                .buildCommands();

        System.out.println("======== Cmd 1 ========");
        command.invoke(
                System.out,
                "cmdLabel", // The command label, Only for @MLabel parameter. Usually it's useless
                List.of("cmd1", "-arg0", "arg1xx")
        );

        System.out.println("======== Cmd 2 ========");
        command.invoke(
                System.out,
                "cmdLabel", // The command label, Only for @MLabel parameter. Usually it's useless
                List.of("cmd2", "arg1", "arg2", "arg3", "-arg", "ArgValue", "-argnotfound", "agagx")
        );

        System.out.println("======== Cmd 3 ========");
        command.invoke(
                System.out,
                "cmdLabel", // The command label, Only for @MLabel parameter. Usually it's useless
                List.of("cmd3")
        );

        System.out.println("======== Cmd 4 ========");
        command.invoke(
                System.out,
                "cmdLabel", // The command label, Only for @MLabel parameter. Usually it's useless
                List.of("cmd4")
        );
        command.invoke(
                System.out,
                "cmdLabel", // The command label, Only for @MLabel parameter. Usually it's useless
                List.of("subcmd","cmd4")
        );
    }
}
