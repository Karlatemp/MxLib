/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.test/TestFormatter.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package format;

import io.github.karlatemp.mxlib.format.FormatArguments;
import io.github.karlatemp.mxlib.format.common.SimpleFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestFormatter {
    @Test
    void testSimpleFormatter() {
        var instance = SimpleFormatter.INSTANCE;
        Assertions.assertEquals(
                instance.parse("Welcome {0} to {1}")
                        .format(FormatArguments.by("Karlatemp", "MxLib"))
                        .to_string(),
                "Welcome Karlatemp to MxLib"
        );
        Assertions.assertEquals(
                instance.parse("Welcome {0} to {1} Ending")
                        .format(FormatArguments.by("Karlatemp", "MxLib"))
                        .to_string(),
                "Welcome Karlatemp to MxLib Ending"
        );
        Assertions.assertEquals(
                instance.parse("{0}{1}")
                        .format(FormatArguments.by("Karla", "temp"))
                        .to_string(),
                "Karlatemp"
        );
    }
}
