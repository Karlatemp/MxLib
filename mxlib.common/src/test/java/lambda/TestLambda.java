/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.test/TestLambda.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package lambda;

import io.github.karlatemp.mxlib.reflect.LambdaFactory;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Consumer;

public class TestLambda {
    interface Texz {
        String apply(Object x);
    }

    public static String valueOf(Object value) {
        Thread.dumpStack();
        return String.valueOf(value);
    }

    public static void collect(Object value) {
        System.out.println("Collected: " + value);
        new Exception("Thread dump").printStackTrace(System.out);
    }

    static Object filter(Object value) {
        return value + " do filter";
    }

    @SuppressWarnings("unchecked")
    @Test
    void testLambdaFactory() throws Throwable {
        MethodHandles.Lookup caller = MethodHandles.lookup();
        MethodHandle valueOf = caller.findStatic(TestLambda.class, "valueOf", MethodType.methodType(String.class, Object.class));
        MethodHandle filter = caller.findStatic(TestLambda.class, "filter", MethodType.methodType(Object.class, Object.class));
        MethodHandle collect = caller.findStatic(TestLambda.class, "collect", MethodType.methodType(void.class, Object.class));

        MethodHandle handle = MethodHandles.collectArguments(valueOf, 0, filter);

        System.out.println(handle.invoke("Hello! "));
        Texz bridge = LambdaFactory.bridge(Texz.class, caller, handle);

        System.out.println(bridge);
        System.out.println(bridge.apply("Hello world"));

        LambdaFactory.bridge(Consumer.class, caller,
                MethodHandles.collectArguments(collect, 0, filter)
        ).accept("Hi!");
    }
}
