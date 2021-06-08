/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.test/TestStringUtil.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package util;

import io.github.karlatemp.mxlib.utils.StringUtils;
import io.github.karlatemp.mxlib.utils.StringUtils.BkColors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestStringUtil {
    @Test
    void testBkTrans() {
        var msg = BkColors._1 + "Hello " + BkColors._B + "World";
        var bkmsg = "§1Hello §bWorld";
        Assertions.assertEquals(bkmsg, BkColors.replaceToBkMessage(msg));
        Assertions.assertEquals(msg, BkColors.replaceToAnsiMsg(bkmsg));
        Assertions.assertEquals("§k123", BkColors.replaceToAnsiMsg("§k123"));
    }

    @Test
    void testParseUnicode() {
        var msg = "\\u00a7114514\\u00a7";
        Assertions.assertEquals("\u00a7114514\u00a7", StringUtils.parseUnicode(msg));
    }
}
