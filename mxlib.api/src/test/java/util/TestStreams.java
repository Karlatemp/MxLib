/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.test/TestStreams.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package util;

import io.github.karlatemp.mxlib.utils.Streams;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TestStreams {
    @Test
    void testStream() {
        Streams.iterate(Arrays.asList("1", "2", "487", "87waex").iterator())
                .forEach(System.out::println);
    }
}
