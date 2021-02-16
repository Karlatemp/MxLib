/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/ClassInfoImpl.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.impl;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.ConstructorInfo;
import io.github.karlatemp.mxlib.classmodel.FieldInfo;
import io.github.karlatemp.mxlib.classmodel.MethodInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

@ApiStatus.Internal
public class ClassInfoImpl implements ClassInfo {


    public static ClassInfo ofClass(Class<?> type) {
        return ofClass(new HashMap<>(), type);
    }

    private static void collectInterfaces(Set<Class<?>> interfaces, Class<?> type) {
        if (type == null) return;
        if (type.isInterface()) {
            interfaces.add(type);
        }
        collectInterfaces(interfaces, type.getSuperclass());
        Class<?>[] interfaces1 = type.getInterfaces();
        if (interfaces1 != null) {
            for (Class<?> i : interfaces1) {
                collectInterfaces(interfaces, i);
            }
        }
    }

    private static Class<?> peekVmClass(Map<Class<?>, ClassInfo> caches, Class<?> type) {
        if (caches == ConstantPools.BUILT_IN) {
            return type;
        }
        return null;
    }

    @SuppressWarnings("DuplicatedCode")
    public static ClassInfo ofClass(Map<Class<?>, ClassInfo> caches, Class<?> type) {
        if (type == null) return null;
        ClassInfo info = caches.get(type);
        if (info != null) return info;
        ClassInfo buildInInfo = ConstantPools.BUILT_IN.get(type);
        if (buildInInfo != null) return buildInInfo;

        if (type.isPrimitive()) {
            ClassInfoImpl result = new ClassInfoImpl(
                    type.getName(), null,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Modifier.PUBLIC,
                    type
            );
            caches.put(type, result);
            return result;
        }

        if (type.isArray()) {
            ClassInfo ofClass = ofClass(caches, type.getComponentType());
            ClassInfo result = ofClass.arrayType();
            caches.put(type, result);
            return result;
        }

        String name = type.getName();
        ClassInfo superType = ofClass(caches, type.getSuperclass());
        List<MethodInfo> methods = new ArrayList<>();
        List<FieldInfo> fields = new ArrayList<>();
        List<ClassInfo> interfaces = new ArrayList<>();
        List<ConstructorInfo> constructors = new ArrayList<>();
        ClassInfoImpl result = new ClassInfoImpl(
                name,
                superType,
                Collections.unmodifiableList(methods),
                Collections.unmodifiableList(fields),
                Collections.unmodifiableList(constructors),
                Collections.unmodifiableList(interfaces),
                type.getModifiers(),
                peekVmClass(caches, type)
        );
        caches.put(type, result);
        Set<Class<?>> interfaces0 = new HashSet<>();
        collectInterfaces(interfaces0, type);
        interfaces0.remove(type);

        for (Class<?> i : interfaces0) {
            interfaces.add(ofClass(caches, i));
        }
        for (Field field : type.getDeclaredFields()) {
            fields.add(new FieldInfoImpl(ofClass(caches, field.getType()), result, field.getModifiers(), field.getName()));
        }

        for (Constructor<?> c : type.getDeclaredConstructors()) {
            List<ClassInfo> argTypes = new ArrayList<>(), thrTypes = new ArrayList<>();
            for (Class<?> arg : c.getParameterTypes()) {
                argTypes.add(ofClass(caches, arg));
            }
            Class<?>[] exceptionTypes = c.getExceptionTypes();
            if (exceptionTypes != null) {
                for (Class<?> e : exceptionTypes) {
                    thrTypes.add(ofClass(caches, e));
                }
            }
            constructors.add(new ConstructorInfoImpl(
                    Collections.unmodifiableList(argTypes),
                    Collections.unmodifiableList(thrTypes),
                    result,
                    c.getModifiers()
            ));
        }

        for (Method method : type.getDeclaredMethods()) {
            List<ClassInfo> argTypes = new ArrayList<>(), thrTypes = new ArrayList<>();
            for (Class<?> arg : method.getParameterTypes()) {
                argTypes.add(ofClass(caches, arg));
            }
            Class<?>[] exceptionTypes = method.getExceptionTypes();
            if (exceptionTypes != null) {
                for (Class<?> e : exceptionTypes) {
                    thrTypes.add(ofClass(caches, e));
                }
            }
            methods.add(new MethodInfoImpl(
                    method.getName(),
                    method.getModifiers(),
                    result,
                    ofClass(caches, method.getReturnType()),
                    Collections.unmodifiableList(argTypes),
                    Collections.unmodifiableList(thrTypes)
            ));
        }

        return result;
    }

    public static class ConstantPools {
        public static final Map<Class<?>, ClassInfo> BUILT_IN = new HashMap<>();
        public static final ClassInfo INT, LONG, SHORT, VOID, DOUBLE, FLOAT, CHAR, BYTE, BOOLEAN, OBJECT, STRING;

        static {
            INT = ofClass(BUILT_IN, int.class);
            LONG = ofClass(BUILT_IN, long.class);
            SHORT = ofClass(BUILT_IN, short.class);
            VOID = ofClass(BUILT_IN, void.class);
            DOUBLE = ofClass(BUILT_IN, double.class);
            FLOAT = ofClass(BUILT_IN, float.class);
            CHAR = ofClass(BUILT_IN, char.class);
            BYTE = ofClass(BUILT_IN, byte.class);
            BOOLEAN = ofClass(BUILT_IN, boolean.class);
            OBJECT = ofClass(BUILT_IN, Object.class);
            STRING = ofClass(BUILT_IN, String.class);
        }
    }

    private final int modifiers;
    private final String name;
    private final ClassInfo superType;
    private final List<MethodInfo> methods;
    private final List<FieldInfo> fields;
    private final List<ConstructorInfo> constructors;
    private final List<ClassInfo> interfaces;
    public final Class<?> vmClass;

    public ClassInfoImpl(
            String name,
            ClassInfo superType,
            List<MethodInfo> methods,
            List<FieldInfo> fields,
            List<ConstructorInfo> constructors,
            List<ClassInfo> interfaces,
            int modifier,
            Class<?> vmClass
    ) {
        this.name = name;
        this.superType = superType;
        this.methods = methods;
        this.fields = fields;
        this.constructors = constructors;
        this.modifiers = modifier;
        this.interfaces = interfaces;
        this.vmClass = vmClass;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public @NotNull ClassInfo arrayType() {
        return new ArrayClassInfo(this);
    }

    @Override
    public ClassInfo getComponentType() {
        return null;
    }

    @Override
    public @Nullable ClassInfo getSuperclass() {
        return superType;
    }

    @Override
    public @NotNull List<ClassInfo> getInterfaces() {
        return interfaces;
    }

    @Override
    public @NotNull List<MethodInfo> getMethods() {
        List<MethodInfo> result = new ArrayList<>(methods);
        for (ClassInfo interfaceInfo : getInterfaces()) {
            result.addAll(interfaceInfo.getDeclaredMethods());
        }

        ClassInfo scan0 = getSuperclass();
        while (scan0 != null) {
            result.addAll(scan0.getDeclaredMethods());
            scan0 = scan0.getSuperclass();
        }
        return result;
    }

    @NotNull
    @Override
    public List<ConstructorInfo> getConstructors() {
        return constructors;
    }

    @Override
    public @NotNull List<MethodInfo> getDeclaredMethods() {
        return methods;
    }

    @Override
    public @NotNull List<FieldInfo> getFields() {
        List<FieldInfo> result = new ArrayList<>(fields);
        ClassInfo scan0 = superType;
        while (scan0 != null) {
            result.addAll(scan0.getDeclaredFields());
            scan0 = scan0.getSuperclass();
        }
        return result;
    }

    @Override
    public @NotNull List<FieldInfo> getDeclaredFields() {
        return fields;
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (Modifier.isPublic(modifiers)) {
            builder.append("public ");
        } else if (Modifier.isProtected(modifiers)) {
            builder.append("protected ");
        } else {
            builder.append("package-private ");
        }
        if (Modifier.isAbstract(modifiers)) {
            if ((modifiers & 0x2000) != 0) {
                builder.append("@interface ");
            } else if (Modifier.isInterface(modifiers)) {
                builder.append("interface ");
            } else {
                builder.append("abstract ");
            }
        }
        if (Modifier.isFinal(modifiers)) {
            builder.append("final ");
        }
        if ((modifiers & 0x1000) != 0) {
            builder.append("synthetic ");
        }
        if (Modifier.isInterface(modifiers)) {
            if (!Modifier.isAbstract(modifiers) && (modifiers & 0x2000) == 0) {
                builder.append("interface ");
            }
        } else if ((modifiers & 0x4000) != 0) {
            builder.append("enum ");
        } else {
            builder.append("class ");
        }
        if ((modifiers & 0x8000) != 0) {
            builder.append("module ");
        }
        builder.append(getName());

        return builder.toString();
    }

    public static class ArrayClassInfo extends ClassInfoImpl {
        private final ClassInfoImpl component;
        private final List<FieldInfo> fields;

        public ArrayClassInfo(ClassInfoImpl component) {
            super(null, null,
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Collections.emptyList(),
                    Modifier.PUBLIC,
                    null
            );

            this.component = component;
            this.fields = Collections.singletonList(new FieldInfoImpl(ConstantPools.INT, this, Modifier.PUBLIC | Modifier.FINAL, "length"));
        }

        @Override
        public boolean isArray() {
            return true;
        }

        @Override
        public @Nullable ClassInfo getSuperclass() {
            if (component == ConstantPools.OBJECT)
                return component;
            ClassInfo superType = component.superType;
            if (superType == null) return ConstantPools.OBJECT;
            return superType.arrayType();
        }

        @Override
        public ClassInfo getComponentType() {
            return component;
        }

        @Override
        public @NotNull List<FieldInfo> getDeclaredFields() {
            return fields;
        }

        @Override
        public @NotNull List<FieldInfo> getFields() {
            return getDeclaredFields();
        }

        @Override
        public @NotNull String getName() {
            return component.getName() + "[]";
        }

        @Override
        public String toString() {
            return "class " + component.getName() + "[]";
        }
    }
}
