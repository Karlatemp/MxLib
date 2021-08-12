/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/Reflections.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.reflect;

import io.github.karlatemp.caller.CallerFinder;
import io.github.karlatemp.caller.StackFrame;
import io.github.karlatemp.mxlib.utils.Predicates;
import io.github.karlatemp.unsafeaccessor.Root;
import io.github.karlatemp.unsafeaccessor.UnsafeAccess;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

@SuppressWarnings({"unchecked", "OptionalGetWithoutIsPresent"})
public class Reflections {
    private static final UnsafeAccess UA = UnsafeAccess.getInstance();

    public static <T> T allocObject(Class<T> aClass) {
        try {
            return (T) Root.getUnsafe().allocateInstance(aClass);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    enum AccessibleObjectOpenAccessImpl implements AccessibleObjectOpenAccess<Object> {
        INSTANCE;

        @Override
        public void accept(Object member) {
            if (member instanceof AccessibleObject)
                Root.openAccess((AccessibleObject) member);
        }

        public Object apply(Object member) {
            accept(member);
            return member;
        }
    }

    public interface AccessibleObjectOpenAccess<T> extends Function<T, T>, Consumer<T> {
        @Override
        @Contract(value = "null -> null; !null -> !null", pure = false)
        T apply(T t);
    }

    public enum Filters implements Predicate<Object> {
        FIELD(Field.class),
        METHOD(Method.class),
        CONSTRUCTOR(Constructor.class),
        ;

        Filters(Class<?> type) {
            this.type = type;
        }

        Class<?> type;

        @Override
        public boolean test(Object o) {
            return type.isInstance(o);
        }
    }

    public enum ModifierFilter implements Predicate<Member> {
        PRIVATE(Modifier.PRIVATE, 0, 0),
        PUBLIC(Modifier.PUBLIC, 0, 0),
        STATIC(Modifier.STATIC, 0, 0),
        NON_STATIC(0, Modifier.STATIC, 0),
        NON_PRIVATE(0, Modifier.STATIC, 0),
        NON_PUBLIC(0, Modifier.PUBLIC, 0),
        PRIVATE_OR_PACKAGE(0, Modifier.PUBLIC | Modifier.PROTECTED, 0),

        ;

        ModifierFilter(int required, int excluded, int any) {
            this.required = required;
            this.excluded = excluded;
            this.any = any;
        }

        int required;
        int excluded;
        int any;

        @Override
        public boolean test(Member member) {
            int modifier = member.getModifiers();
            return (modifier & required) == required && (modifier & excluded) == 0 && (any == 0 || (modifier & any) != 0);
        }
    }

    public static Stream<Member> allMembers(Class<?> klass) {
        ArrayList<Member> members = new ArrayList<>(16 * 3);
        while (klass != null) {
            members.addAll(Arrays.asList(klass.getDeclaredFields()));
            members.addAll(Arrays.asList(klass.getDeclaredMethods()));
            klass = klass.getSuperclass();
        }
        return members.stream();
    }

    public static @NotNull <T> AccessibleObjectOpenAccess<T> openAccess() {
        return (AccessibleObjectOpenAccess<T>) AccessibleObjectOpenAccessImpl.INSTANCE;
    }

    public static @NotNull Stream<Field> allFields(Class<?> klass) {
        return (Stream<Field>) (Stream<?>) allMembers(klass)
                .filter(Filters.FIELD);
    }

    public static @NotNull Stream<Method> allMethods(Class<?> klass) {
        return (Stream<Method>) (Stream<?>) allMembers(klass)
                .filter(Filters.METHOD);
    }

    public static @NotNull <T extends AnnotatedElement> Predicate<T> withAnnotated(
            Class<? extends Annotation> type
    ) {
        return it -> it.getDeclaredAnnotation(type) != null;
    }

    public static @NotNull <T extends Member> Predicate<T> withName(
            String name
    ) {
        if (name == null) return Predicates.alwaysTrue();
        return it -> it.getName().equals(name);
    }

    public static ClassLoader getClassLoader(StackFrame caller) {
        if (caller == null) return null;
        Class<?> instance = caller.getClassInstance();
        if (instance == null) return null;
        return instance.getClassLoader();
    }

    public static ClassLoader getClassLoader(Class<?> klass) {
        if (klass != null) return klass.getClassLoader();
        return null;
    }

    public static @NotNull Optional<Method> findMethod$kt(
            Class<?> klass,
            String name,
            boolean isStatic,
            Class<?> returnType,
            Class<?>[] parameterTypes
    ) {
        return findMethod(klass, name, isStatic, returnType, parameterTypes);
    }

    public static @NotNull Optional<Method> findMethod(
            Class<?> klass,
            String name,
            boolean isStatic,
            Class<?> returnType,
            Class<?>... parameterTypes
    ) {
        Stream<Method> stream = allMethods(klass)
                .filter(isStatic ? ModifierFilter.STATIC : ModifierFilter.NON_STATIC)
                .filter(withName(name));
        if (returnType != null) {
            stream = stream.filter(it -> returnType == it.getReturnType());
        }
        if (parameterTypes != null) {
            stream = stream.filter(it -> Arrays.equals(parameterTypes, it.getParameterTypes()));
        }
        return stream.findFirst();
    }

    public static @NotNull Optional<Field> findField(
            Class<?> klass,
            String name,
            boolean isStatic,
            Class<?> type
    ) {
        Stream<Field> stream = allFields(klass)
                .filter(isStatic ? ModifierFilter.STATIC : ModifierFilter.NON_STATIC)
                .filter(withName(name));
        if (type != null) {
            stream = stream.filter(it -> type.equals(it.getType()));
        }
        return stream.findFirst();
    }

    public static MethodHandle mapToHandle(Method method) {
        try {
            return UA.getTrustedIn(method.getDeclaringClass()).unreflect(method);
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static <T> T bindTo(
            MethodHandles.Lookup lookup,
            MethodHandle handle,
            Class<T> interfaceClass,
            MethodType samMethodType,
            String funcName
    ) throws Throwable {
        if (handle == null) return null;
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException(interfaceClass + " must be a interface.");
        }
        return (T) LambdaMetafactory.metafactory(
                lookup,
                funcName,
                MethodType.methodType(interfaceClass),
                samMethodType,
                handle,
                handle.type()
        ).getTarget().invoke();
    }

    public static <T> T bindTo(
            MethodHandle handle,
            Class<T> interfaceClass,
            MethodType samMethodType,
            String funcName
    ) throws Throwable {
        Class<?> caller = CallerFinder.getCaller().getClassInstance();
        return bindTo(
                UA.getTrustedIn(caller).in(caller),
                handle, interfaceClass, samMethodType, funcName
        );
    }

    public static <T> T bindToNoErr(
            MethodHandle handle,
            Class<T> interfaceClass,
            MethodType samMethodType,
            String funcName
    ) {
        Class<?> caller = CallerFinder.getCaller().getClassInstance();
        return bindToNoErr(
                UA.getTrustedIn(caller).in(caller),
                handle, interfaceClass, samMethodType, funcName
        );
    }

    public static <T> T bindToNoErr(
            MethodHandles.Lookup lookup,
            MethodHandle handle,
            Class<T> interfaceClass,
            MethodType samMethodType,
            String funcName
    ) {
        try {
            return bindTo(lookup, handle, interfaceClass, samMethodType, funcName);
        } catch (Error | RuntimeException e) {
            throw e;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    static class ClassloaderInjector {
        private static final List<BiPredicate<ClassLoader, File>> injectors = new ArrayList<>();

        static class URLClassLoaderInjector implements BiPredicate<ClassLoader, File> {
            static final BiConsumer<Object, Object> ADD_URL = findMethod(
                    URLClassLoader.class,
                    "addURL", false,
                    void.class, URL.class
            ).map(method -> bindToNoErr(
                    UA.getTrustedIn(URLClassLoader.class).in(URLClassLoader.class),
                    mapToHandle(method),
                    BiConsumer.class,
                    MethodType.methodType(void.class, Object.class, Object.class),
                    "accept"
            )).get();

            @Override
            public boolean test(ClassLoader classLoader, File file) {
                if (classLoader instanceof URLClassLoader) {
                    try {
                        ADD_URL.accept(classLoader, file.toURI().toURL());
                        return true;
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }
                return false;
            }
        }

        static class SysClassLoaderInject implements BiPredicate<ClassLoader, File> {
            private final Class<?> BuiltinClassLoader;
            private final Field BCL$ucp;
            private final Method URLClassPath$addURL;

            SysClassLoaderInject() throws Throwable {
                Class<?> BuiltinClassLoader = Class.forName("jdk.internal.loader.BuiltinClassLoader");
                Class<?> URLClassPath = Class.forName("jdk.internal.loader.URLClassPath");
                Field BCL$ucp = findField(BuiltinClassLoader, null, false, URLClassPath).get();
                Root.openAccess(BCL$ucp);
                Method URLClassPath$addURL = URLClassPath.getDeclaredMethod("addURL", URL.class);
                Root.openAccess(URLClassPath$addURL);

                this.BuiltinClassLoader = BuiltinClassLoader;
                this.BCL$ucp = BCL$ucp;
                this.URLClassPath$addURL = URLClassPath$addURL;
            }

            @Override
            public boolean test(ClassLoader classLoader, File file) {
                if (BuiltinClassLoader.isInstance(classLoader)) {
                    try {
                        URL url = file.toURI().toURL();
                        URLClassPath$addURL.invoke(BCL$ucp.get(classLoader), url);
                        return true;
                    } catch (Throwable ignore) {
                    }
                }
                return false;
            }
        }

        static {
            injectors.add(new URLClassLoaderInjector());
            try {
                injectors.add(new SysClassLoaderInject());
            } catch (Throwable ignored) {
            }
        }
    }

    public static void addToClassLoader(ClassLoader classLoader, File file) {
        for (BiPredicate<ClassLoader, File> loader : ClassloaderInjector.injectors) {
            if (loader.test(classLoader, file)) {
                return;
            }
        }
        throw new NoSuchElementException("Not support to inject " + classLoader);
    }
}
