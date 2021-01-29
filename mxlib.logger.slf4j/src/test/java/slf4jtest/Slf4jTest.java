/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger-slf4j.test/Slf4jTest.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package slf4jtest;

import io.github.karlatemp.mxlib.logger.slf4j.MSlf4JLoggerInstaller;
import io.github.karlatemp.mxlib.logger.slf4j.MSlf4jLoggerFactory;
import logger.TestRawMessageFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

public class Slf4jTest {
    public static void main(String[] args) throws Exception {
        ILoggerFactory factory = new MSlf4jLoggerFactory(TestRawMessageFactory.Lg::new);
        MSlf4JLoggerInstaller.install(factory);
        LoggerFactory.getLogger("Test").info("Hi!");
        LoggerFactory.getLogger("Tsfx").info("Hi!", new Exception("FAQ"));
    }
}
