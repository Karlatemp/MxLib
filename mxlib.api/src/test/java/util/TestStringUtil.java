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

import io.github.karlatemp.mxlib.utils.StringUtils.BkColors;
import org.junit.jupiter.api.Test;

public class TestStringUtil {
    @Test
    void testBkTrans() {
        System.out.println(BkColors.replaceToBkMessage(BkColors._1 + "Hello " + BkColors._B + "World"));
    }
}
