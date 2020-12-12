package io.github.karlatemp.mxlib.logger.utils;

import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.mxlib.utils.Functions;
import io.github.karlatemp.mxlib.utils.Predicates;
import io.github.karlatemp.unsafeaccessor.Root;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class StackTraceElementUtils {
    private static final MethodHandles.Lookup LOOKUP = Root.getTrusted().in(StackTraceElement.class);

    // dropClassLoaderName
    public static final Predicate<StackTraceElement> DROP_CLASS_LOADER_NAME = Reflections.findMethod(
            StackTraceElement.class,
            "dropClassLoaderName",
            false,
            boolean.class
    ).map(method -> Reflections.bindToNoErr(
            LOOKUP,
            Reflections.mapToHandle(method),
            Predicate.class,
            MethodType.methodType(boolean.class, Object.class),
            "test"
    )).orElse(Predicates.alwaysFalse());

    // dropModuleVersion
    public static final Predicate<StackTraceElement> DROP_MODULE_VERSION = Reflections.findMethod(
            StackTraceElement.class,
            "dropModuleVersion",
            false,
            boolean.class
    ).map(method -> Reflections.bindToNoErr(
            LOOKUP,
            Reflections.mapToHandle(method),
            Predicate.class,
            MethodType.methodType(boolean.class, Object.class),
            "test"
    )).orElse(Predicates.alwaysFalse());

    public static final Function<StackTraceElement, String> GET_CLASSLOADER_NAME = Reflections.findMethod(
            StackTraceElement.class,
            "getClassLoaderName",
            false,
            String.class
    ).map(method -> Reflections.bindToNoErr(
            LOOKUP,
            Reflections.mapToHandle(method),
            Function.class,
            MethodType.methodType(Object.class, Object.class),
            "apply"
    )).orElse(Functions.alwaysNull());

    public static final Function<StackTraceElement, String> GET_MODULE_NAME = Reflections.findMethod(
            StackTraceElement.class,
            "getModuleName",
            false,
            String.class
    ).map(method -> Reflections.bindToNoErr(
            LOOKUP,
            Reflections.mapToHandle(method),
            Function.class,
            MethodType.methodType(Object.class, Object.class),
            "apply"
    )).orElse(Functions.alwaysNull());

    public static final Function<StackTraceElement, String> GET_MODULE_VERSION = Reflections.findMethod(
            StackTraceElement.class,
            "getModuleVersion",
            false,
            String.class
    ).map(method -> Reflections.bindToNoErr(
            LOOKUP,
            Reflections.mapToHandle(method),
            Function.class,
            MethodType.methodType(Object.class, Object.class),
            "apply"
    )).orElse(Functions.alwaysNull());
}
