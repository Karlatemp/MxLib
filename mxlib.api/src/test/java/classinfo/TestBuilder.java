/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.test/TestBuilder.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package classinfo;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.analysis.ClassInfoDumper;
import io.github.karlatemp.mxlib.classmodel.builder.ClassInfoBuilder;
import io.github.karlatemp.mxlib.classmodel.resolve.Resolver;
import io.github.karlatemp.mxlib.utils.LineWriter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class TestBuilder {
    public void myFunc() {
    }

    @Test
    void a() throws Exception {
        ClassInfoBuilder builder = ClassInfoBuilder.newBuilder()
                .name("classinfo.TestBuilder");
        builder.method(it -> it.name("myFunc")
                .returnType(ClassInfo.ofClass(void.class))
        );
        ClassInfoDumper.dump(builder.result(), LineWriter.from(System.out));
        Method resolve = Resolver.DEFAULT.withLoader(getClass().getClassLoader())
                .resolve(builder.result().getDeclaredMethods().get(0));
        System.out.println(resolve);
    }
}
