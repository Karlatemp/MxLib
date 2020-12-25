package io.github.karlatemp.mxlib.common.utils;

import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.unsafeaccessor.Root;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ToolkitCommon {
    private static final MethodHandles.Lookup LOOKUP = Root.getTrusted();
    public static final Function<Enumeration<?>, Iterator<?>> AS_ITERATOR = Reflections.findMethod(
            Enumeration.class,
            "asIterator",
            false,
            Iterator.class
    ).map(method -> Reflections.bindToNoErr(
            LOOKUP,
            Reflections.mapToHandle(method),
            Function.class,
            MethodType.methodType(Object.class, Object.class),
            "apply"
    )).orElse((Function<Enumeration<?>, ?>) (Enumeration<?> enu) -> {
        if (enu instanceof Iterator<?>) return enu;
        return new Iterator() {
            @Override
            public boolean hasNext() {
                return enu.hasMoreElements();
            }

            @Override
            public Object next() {
                return enu.nextElement();
            }
        };
    });

    public static <T> Iterator<T> asIterator(Enumeration<T> entries) {
        return (Iterator<T>) AS_ITERATOR.apply(entries);
    }
}
