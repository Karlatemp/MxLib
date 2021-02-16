/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.test/TestClassInfo.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package classinfo;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.FieldInfo;
import io.github.karlatemp.mxlib.classmodel.MethodInfo;
import io.github.karlatemp.mxlib.classmodel.analysis.AbstractMethodAnalyzer;
import io.github.karlatemp.mxlib.classmodel.analysis.ClassInfoDumper;
import io.github.karlatemp.mxlib.classmodel.impl.ClassInfoImpl;
import io.github.karlatemp.mxlib.classmodel.resolve.Resolver;
import io.github.karlatemp.mxlib.utils.LineWriter;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TestClassInfo {

    //    @Test
    void runTest() {
        Map<Class<?>, ClassInfo> builtIn = ClassInfoImpl.ConstantPools.BUILT_IN;
        for (Map.Entry<Class<?>, ClassInfo> entry : builtIn.entrySet()) {
            ClassInfo classInfo = entry.getValue();
            if (!classInfo.isArray()) {
                continue;
            }
            System.out.println(classInfo);
            for (MethodInfo methodInfo : classInfo.getMethods()) {
                System.out.println("> " + methodInfo);
            }
            for (FieldInfo fieldInfo : classInfo.getFields()) {
                System.out.println("> " + fieldInfo);
            }
        }
    }

    interface Steexx extends Function<String, String> {
        String apffww(String asdw);

        @Override
        default String apply(String s) {
            return apffww(s);
        }
    }

    interface Fwadxw {
        String apffww(String asdw);
    }

    interface AWQQ extends Fwadxw, Steexx {

    }

    static class Aexxxer {
        String awzadrff;
        int awxrrr;

        public String apply(String s) {
            return null;
        }
    }

    static abstract class Wxiawrr extends Aexxxer implements AWQQ {
        String aseaxzxww;

        @Override
        public String apffww(String asdw) {
            return null;
        }

        abstract String ovvvov(Object awexxx);
    }

    @Test
    void runLambdaFind() throws Throwable {
        ClassInfo info = ClassInfo.ofClass(Wxiawrr.class);
        ClassInfoDumper.dump(info, LineWriter.from(System.out));

        System.out.println("Abstract methods:");
        List<MethodInfo> abstractMethods = AbstractMethodAnalyzer.findAbstractMethods(info.getMethods());
        for (MethodInfo m : abstractMethods) {
            System.out.println(m.getDeclaredClass() + " > " + m);
        }
        MethodInfo abstractMethod = AbstractMethodAnalyzer.findSingleAbstractMethod(abstractMethods);
        System.out.println("SAM: " + abstractMethod);
        System.out.println(Resolver.DEFAULT.withLoader(getClass().getClassLoader()).resolve(abstractMethod));
    }
}
