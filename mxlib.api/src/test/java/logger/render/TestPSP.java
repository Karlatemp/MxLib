/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.test/TestPSP.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package logger.render;

import io.github.karlatemp.mxlib.logger.AwesomeLogger;
import io.github.karlatemp.mxlib.logger.BasicMessageFactory;
import io.github.karlatemp.mxlib.logger.renders.PrefixSupplierBuilder;
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender;
import io.github.karlatemp.mxlib.logger.renders.SimpleRender;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Scanner;

public class TestPSP {
    @Test
    void run() {
        BasicMessageFactory bmf = new BasicMessageFactory();
        var result = PrefixSupplierBuilder.parseFrom(
                new Scanner(Objects.requireNonNull(
                        TestRenders.class.getResourceAsStream("/logger/prefix.txt")
                ))
        );
        PrefixedRender renderX = new PrefixedRender(
                new SimpleRender(bmf), result
        );
        var logger = new AwesomeLogger.Awesome.Awesome("test", System.out::println, renderX);
        logger.info("HelloWorld", new Throwable());
    }
}
