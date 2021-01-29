/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.test/TransTest.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package trans;

import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.logger.AwesomeLogger;
import io.github.karlatemp.mxlib.logger.BasicMessageFactory;
import io.github.karlatemp.mxlib.logger.renders.SimpleRender;
import io.github.karlatemp.mxlib.translate.TranslateLoader;
import io.github.karlatemp.mxlib.translate.Translator;

public class TransTest {
    public static void main(String[] args) {
        MxLib.setLogger(new AwesomeLogger.Awesome(
                "MxLib", System.out::println,
                new SimpleRender(new BasicMessageFactory())
        ));
        Translator translator = new TranslateLoader.WithClassLoader(
                TransTest.class.getClassLoader(),
                "trans/trans",
                (m, e) -> {
                    System.err.println(m);
                    e.printStackTrace();
                }
        ).loadTranslate(null);
        System.out.println(translator);
        System.out.println(translator.format("a").to_string());
        System.out.println(translator.format("b").to_string());
        System.out.println(translator.format("ab").to_string());
    }
}
