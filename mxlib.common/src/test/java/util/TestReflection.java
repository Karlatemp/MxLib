package util;

import io.github.karlatemp.mxlib.reflect.Reflections;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodType;
import java.util.function.Consumer;


public class TestReflection {
    public static void invoke(String name) {
        System.out.println("Ho!!!! " + name);
    }

    @Test
    void testBind() throws Throwable {
        var met = Reflections.findMethod(
                TestReflection.class,
                "invoke",
                true,
                null,
                (Class<?>[]) null
        ).get();
        System.out.println(met);
        var cx = Reflections.bindTo(
                Reflections.mapToHandle(met),
                Consumer.class,
                MethodType.methodType(void.class, Object.class),
                "accept"
        );
        System.out.println(cx);
        cx.accept("Hello!");
    }
}
