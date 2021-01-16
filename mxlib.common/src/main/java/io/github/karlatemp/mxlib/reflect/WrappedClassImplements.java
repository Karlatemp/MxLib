/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/WrappedClassImplements.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.reflect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.*;

import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Wrapped Abstract class.
 *
 * @since 2.8
 */
public class WrappedClassImplements {
    public static int putTypeInsn(Class<?> type, int slot, boolean isReturn, MethodVisitor visitor) {
        return putTypeInsn(Type.getType(type), slot, isReturn, visitor);
    }

    public static int putTypeInsn(Type type, int slot, boolean isReturn, MethodVisitor visitor) {
        switch (type.getSort()) {
            case Type.VOID:
                if (!isReturn) throw new IllegalArgumentException();
                visitor.visitInsn(Opcodes.RETURN);
                break;
            case Type.DOUBLE:
                if (isReturn) visitor.visitInsn(Opcodes.DRETURN);
                else visitor.visitVarInsn(Opcodes.DLOAD, slot);
                break;
            case Type.FLOAT:
                if (isReturn) visitor.visitInsn(Opcodes.FRETURN);
                else visitor.visitVarInsn(Opcodes.FLOAD, slot);
                break;
            case Type.INT:
            case Type.SHORT:
            case Type.BOOLEAN:
            case Type.BYTE:
            case Type.CHAR:
                if (isReturn) visitor.visitInsn(Opcodes.IRETURN);
                else visitor.visitVarInsn(Opcodes.ILOAD, slot);
                break;
            case Type.LONG:
                if (isReturn) visitor.visitInsn(Opcodes.LRETURN);
                else visitor.visitVarInsn(Opcodes.LLOAD, slot);
                break;
            case Type.OBJECT:
            case Type.ARRAY:
                if (isReturn) visitor.visitInsn(Opcodes.ARETURN);
                else visitor.visitVarInsn(Opcodes.ALOAD, slot);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return slot + type.getSize();
    }

    /**
     * Fast create abstract class implement
     *
     * @param path   The implement name
     * @param source The class source
     * @return The implement class
     * @since 2.11
     */
    public static ClassWriter AbstractClassInstance(String path, Class<?> source) {
        ClassWriter cw = new ClassWriter(0);
        // cw.visit(52, Opcodes.ACC_PUBLIC, path, null, source.getName().replace('.', '/'), null);
        if (source.isInterface()) {
            cw.visit(52, Opcodes.ACC_PUBLIC, path, null, "java/lang/Object", new String[]{
                    source.getName().replace('.', '/')
            });
            publicObjectConstructor(cw, "java/lang/Object");
        } else {
            String a;
            cw.visit(52, Opcodes.ACC_PUBLIC, path, null, a = source.getName().replace('.', '/'), null);
            for (Constructor<?> c : source.getDeclaredConstructors()) {
                if (Modifier.isPublic(c.getModifiers()) || Modifier.isProtected(c.getModifiers())) {
                    String desc;
                    final MethodVisitor visitor = cw.visitMethod(
                            c.getModifiers(),
                            "<init>",
                            desc = Type.getConstructorDescriptor(c), null, null
                    );
                    visitor.visitCode();
                    visitor.visitVarInsn(Opcodes.ALOAD, 0);
                    int w = 1;
                    for (Class<?> type : c.getParameterTypes()) {
                        w = putTypeInsn(type, w, false, visitor);
                    }
                    visitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, a, "<init>", desc, false);
                    visitor.visitInsn(Opcodes.RETURN);
                    visitor.visitMaxs(w, w);
                    visitor.visitEnd();
                }
            }
        }
//        publicObjectConstructor(cw, source.getName().replace('.', '/'));
        return cw;
    }

    public static Class<?> getRootAbstractClass(Class<?> own) {
        Class<?> bb = Object.class;
        for (Method m : own.getMethods()) {
            if (Modifier.isAbstract(m.getModifiers())) {
                Class<?> cv = m.getDeclaringClass();
                if (bb.isAssignableFrom(cv)) {
                    bb = cv;
                }
            }
        }
        return bb;
    }

    public static ClassWriter doImplement(Class<?> own) {
        if (!Modifier.isAbstract(own.getModifiers())) {
            throw new UnsupportedOperationException("Only wrap a abstract class.");
        }
        Class<?> bb = getRootAbstractClass(own);
        if (bb == Object.class) {
            throw new UnsupportedOperationException("No any abstract method.");
        }
        String initCon;
        boolean initParam;
        {
            final Constructor<?>[] constructors = own.getDeclaredConstructors();
            Constructor<?> using = null;
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() == 1) {
                    final int modifiers = constructor.getModifiers();
                    if (Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers)) {
                        Class<?> newer = constructor.getParameterTypes()[0];
                        if (newer.isAssignableFrom(bb)) {
                            if (using == null) {
                                using = constructor;
                            } else {
                                Class<?> last = using.getParameterTypes()[0];
                                if (last.isAssignableFrom(newer)) {
                                    using = constructor;
                                }
                            }
                        }
                    }
                }
            }
            if (using == null) {
                for (Constructor<?> c : constructors) {
                    if (c.getParameterCount() == 0) {
                        int mod = c.getModifiers();
                        if (Modifier.isPublic(mod) || Modifier.isProtected(mod))
                            using = c;
                        break;
                    }
                }
            }
            if (using == null)
                throw new UnsupportedOperationException("Cannot found constructor for class(" + bb + ")");
            initCon =
                    '(' + ((initParam = using.getParameterCount() == 1) ?
                            'L' + using.getParameterTypes()[0].getName().replace('.', '/') + ';' :
                            "")
                            + ")V";
        }
        String field = "wrapped_" + UUID.randomUUID().toString().replace('-', '_');
        ClassWriter writer = new ClassWriter(0);
        String createClass = "cn/mcres/karlatemp/mxlib/internal/WrappedClassImplementW" + UUID.randomUUID().toString().replace("-", "");
        String desByteBuffer = 'L' + bb.getName().replace('.', '/') + ';';
        writer.visit(52, Modifier.PUBLIC, createClass, null,
                own.getName().replace('.', '/'), new String[0]);
        writer.visitSource("JVMWrap-" + own.getName(), null);
        writer.visitField(Modifier.PRIVATE, field, desByteBuffer, null, null);
        {
            final MethodVisitor init = writer.visitMethod(Modifier.PUBLIC, "<init>", "(L" + bb.getName().replace('.', '/') + ";)V", null, null);
            init.visitCode();
            init.visitVarInsn(Opcodes.ALOAD, 0);
            if (initParam)
                init.visitVarInsn(Opcodes.ALOAD, 1);
            init.visitMethodInsn(Opcodes.INVOKESPECIAL, own.getName().replace('.', '/'), "<init>", initCon, false);
            init.visitVarInsn(Opcodes.ALOAD, 0);
            init.visitVarInsn(Opcodes.ALOAD, 1);
            init.visitFieldInsn(Opcodes.PUTFIELD, createClass, field, desByteBuffer);
            init.visitInsn(Opcodes.RETURN);
            init.visitMaxs(2, 2);
            init.visitEnd();
        }
        {
            Method[] ovmets = own.getMethods();
            rootLoop:
            for (Method m : bb.getMethods()) {
                if (Modifier.isAbstract(m.getModifiers()) &&
                        (Modifier.isProtected(m.getModifiers()) || Modifier.isPublic(m.getModifiers())) &&
                        !Modifier.isStatic(m.getModifiers())) {
                    for (Method sov : ovmets) {
                        if (!Modifier.isStatic(sov.getModifiers()) && Modifier.isFinal(sov.getModifiers())) {
                            if (sov.getReturnType() == m.getReturnType() && sov.getName().equals(m.getName())) {
                                if (Arrays.equals(sov.getParameterTypes(), m.getParameterTypes())) {
                                    continue rootLoop;
                                }
                            }
                        }
                    }
                    // System.out.println("OMethod: " + m);
                    String DR;
                    final MethodVisitor impl = writer.visitMethod(
                            m.getModifiers() & ~Modifier.ABSTRACT,
                            m.getName(),
                            DR = MethodType.methodType(m.getReturnType(), m.getParameterTypes()).toMethodDescriptorString(),
                            null, Optional.of(m.getExceptionTypes()).filter(
                                    array -> array.length != 0
                            ).map(cArray -> Stream.of(cArray).map(
                                    clazz -> clazz.getName().replace('.', '/')
                            ).toArray(String[]::new)).orElse(null)
                    );
                    impl.visitCode();
                    impl.visitVarInsn(Opcodes.ALOAD, 0);
                    impl.visitFieldInsn(Opcodes.GETFIELD, createClass, field, desByteBuffer);
                    int stacks;
                    {
                        Class<?>[] params = m.getParameterTypes();
                        int w = 1;
                        for (int i = 0; i < params.length; ) {
                            w = putTypeInsn(params[i++], w, false, impl);
                        }
                        stacks = w;
                    }
                    impl.visitMethodInsn(
                            m.getDeclaringClass().isInterface() ?
                                    Opcodes.INVOKEINTERFACE :
                                    Opcodes.INVOKEVIRTUAL, m.getDeclaringClass().getName().replace('.', '/'),
                            m.getName(), DR, m.getDeclaringClass().isInterface());
                    {
                        putTypeInsn(m.getReturnType(), 0, true, impl);
                    }
                    impl.visitMaxs(stacks + 5, 1 + stacks);
                    impl.visitEnd();
                }
            }
        }
//        byte[] created = writer.toByteArray();
//        File f = new File("G:\\IDEAProjects\\MXBukkitLibRebuild\\out\\test.class");
//        f.createNewFile();
//        new FileOutputStream(f).write(created);
//        Class<?> inited = Toolkit.Reflection.defineClass(serializerClass.getClassLoader(), null, created, 0, created.length, null);
//        Object ser = inited.getConstructor(ByteBuf.class).newInstance(io.netty.buffer.Unpooled.buffer());
//        System.out.println(ser);
        return writer;
    }

    /**
     * Create a default constructor.
     *
     * @param visitor    The visitor need created.
     * @param superClass The super class. need internal format
     * @since 2.9
     */
    public static void publicObjectConstructor(ClassVisitor visitor, String superClass) {
        if (superClass == null) superClass = "java/lang/Object";
        final MethodVisitor constructor = visitor.visitMethod(Modifier.PUBLIC, "<init>", "()V", null, null);
        if (constructor != null) {
            constructor.visitCode();
            constructor.visitVarInsn(Opcodes.ALOAD, 0);
            constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, superClass, "<init>", "()V", false);
            constructor.visitInsn(Opcodes.RETURN);
            constructor.visitMaxs(1, 1);
            constructor.visitEnd();
        }
    }

    /**
     * General Implement Internal Class Name
     *
     * @param package_ The package general.
     * @param name     The full class name.
     * @return General class name.
     * @since 2.9
     */
    public static String genImplementClassName(String package_, String name) {
        if (name == null) {
            if (package_ == null) {
                package_ = "cn/mcres/karlatemp/mxlib/internal/implements/";
            }
            if (!package_.endsWith("/")) {
                package_ += '/';
            }
            package_ = package_.replace('.', '/');
            return package_ + UUID.randomUUID().toString().replace('-', '_') + "$" + Long.toHexString(System.currentTimeMillis());
        }
        return name.replace('.', '/');
    }

    /**
     * General implement name
     *
     * @param name The full class name
     * @return Internal class name.
     */
    public static String genImplementClassName(String name) {
        if (name == null) {
            return genImplementClassName(null, null);
        }
        return name;
    }

    public enum MethodWrapperType {
        OBJECT, INTERFACE, STATIC
    }

    public static ClassWriter createMethodWrapper(
            @NotNull Method interface_method,
            @Nullable String proxy_name,
            @NotNull Method target_method
    ) {
        final Class<?> owner = interface_method.getDeclaringClass();
        if (!owner.isInterface()) {
            throw new UnsupportedOperationException(owner + " is not a interface.");
        }
        if (interface_method.getReturnType() != target_method.getReturnType()) {
            throw new UnsupportedOperationException("The return type does not match. Expect (" + target_method.getReturnType() + ") but appear (" + interface_method.getReturnType() + ")");
        }
        MethodWrapperType mwt;
        if (Modifier.isStatic(target_method.getModifiers())) {
            mwt = MethodWrapperType.STATIC;
        } else {
            mwt = target_method.getDeclaringClass().isInterface() ? MethodWrapperType.INTERFACE : MethodWrapperType.OBJECT;
        }
        return createMethodWrapper(
                owner.getName().replace('.', '/'),
                interface_method.getName(),
                Type.getMethodDescriptor(interface_method),
                proxy_name,
                target_method.getDeclaringClass().getName().replace('.', '/'),
                target_method.getName(),
                Type.getMethodDescriptor(target_method),
                mwt
        );
    }

    public static Method getLambdaInterfaceMethod(@NotNull Class<?> lambda_interface) {
        if (!lambda_interface.isInterface()) {
            throw new UnsupportedOperationException("Only support interface proxy.");
        }
        Method interface0 = null;
        for (Method met : lambda_interface.getMethods()) {
            if (met.getDeclaringClass() != Object.class) {
                if (interface0 != null) {
                    throw new UnsupportedOperationException(lambda_interface + " is not lambda interface.");
                }
                interface0 = met;
            }
        }
        if (interface0 == null) throw new UnsupportedOperationException(lambda_interface + " has no methods.");
        return interface0;
    }

    public static ClassWriter createMethodWrapper(
            @NotNull Class<?> lambda_interface,
            @Nullable String proxy_name,
            @NotNull Method target
    ) {
        return createMethodWrapper(
                getLambdaInterfaceMethod(lambda_interface),
                proxy_name,
                target
        );
    }

    public static ClassWriter createMethodWrapper(
            @NotNull Class<?> lambda_interface,
            @Nullable String proxy_name,
            @NotNull String target_internal_source_class_name,
            @NotNull String target_method_name,
            @NotNull MethodWrapperType wrapper_type
    ) {
        Method interface0 = getLambdaInterfaceMethod(lambda_interface);
        return createMethodWrapper(lambda_interface.getName().replace('.', '/'),
                interface0.getName(),
                Type.getMethodDescriptor(interface0),
                proxy_name,
                target_internal_source_class_name,
                target_method_name,
                wrapper_type
        );
    }

    public static ClassWriter createMethodWrapper(
            @NotNull String interface_,
            @NotNull String interface_method_name,
            @NotNull String interface_method_desc,
            @Nullable String proxy_name,
            @NotNull String target_internal_source_class_name,
            @NotNull String target_method_name,
            @NotNull MethodWrapperType wrapper_type) {
        return createMethodWrapper(interface_, interface_method_name, interface_method_desc, proxy_name, target_internal_source_class_name, target_method_name, null, wrapper_type);
    }

    public static final int MATCH_STATE_SAME = 0;
    public static final int MATCH_STATE_CAN_OVERRIDE = 1;
    public static final int MATCH_STATE_CANNOT_SET = 2;

    public static int getMatchState(@NotNull Type source, @NotNull Type target) {
        if (source.equals(target)) return MATCH_STATE_SAME;
        switch (source.getSort()) {
            case Type.OBJECT: {
                if (target.getSort() == Type.OBJECT) {
                    return MATCH_STATE_CAN_OVERRIDE;
                }
                return MATCH_STATE_CANNOT_SET;
            }
            case Type.ARRAY: {
                if (target.getSort() == Type.OBJECT) {
                    if (target.getInternalName().equals("java/lang/Object")) {
                        return MATCH_STATE_CAN_OVERRIDE;
                    }
                } else if (target.getSort() == Type.ARRAY) {
                    return getMatchState(source.getElementType(), target.getElementType());
                }
                return MATCH_STATE_CANNOT_SET;
            }
        }
        return MATCH_STATE_CANNOT_SET;
    }

    private static UnsupportedOperationException createMethodWrapper$error(
            String target_method_desc, String interface_method_desc, String reason
    ) {
        UnsupportedOperationException u = new UnsupportedOperationException("Types not match. " + target_method_desc + " -> " + interface_method_desc);
        if (reason != null) {
            u.addSuppressed(new Exception(reason));
        }
        return u;
    }

    public static ClassWriter createMethodWrapper(
            @NotNull String interface_,
            @NotNull String interface_method_name,
            @NotNull String interface_method_desc,
            @Nullable String proxy_name,
            @NotNull String target_internal_source_class_name,
            @NotNull String target_method_name,
            @Nullable String target_method_desc,
            @NotNull MethodWrapperType wrapper_type) {
        ClassWriter cw = new ClassWriter(0);
        String pn = genImplementClassName(proxy_name);
        cw.visit(52, Modifier.PUBLIC, pn, null, "java/lang/Object", new String[]{interface_});
        {
            int i = pn.lastIndexOf('/');
            if (i == -1)
                cw.visitSource(pn + ".java", null);
            else cw.visitSource(pn.substring(i + 1) + ".java", null);
        }
        publicObjectConstructor(cw, null);
        final MethodVisitor impl = cw.visitMethod(Modifier.PUBLIC, interface_method_name, interface_method_desc, null, null);
        int sx, sw;
        impl.visitCode();
        final Type[] target_argument_types;
        final Type[] interface_argument_types = Type.getArgumentTypes(interface_method_desc);
        final Type interface_method_return_type = Type.getReturnType(interface_method_desc);
        final Type target_method_return_type;
        {
            if (target_method_desc != null) {
                target_argument_types = Type.getArgumentTypes(target_method_desc);
                target_method_return_type = Type.getReturnType(target_method_desc);
                if (wrapper_type == MethodWrapperType.STATIC) {
                    if (target_argument_types.length != interface_argument_types.length) {
                        throw createMethodWrapper$error(target_method_desc, interface_method_desc, "Parameters length not match.");
                    }
                    for (int i = 0; i < target_argument_types.length; i++) {
                        Type source = interface_argument_types[i];
                        Type target_ = target_argument_types[i];
                        if (getMatchState(source, target_) == MATCH_STATE_CANNOT_SET) {
                            throw createMethodWrapper$error(target_method_desc, interface_method_desc, source.getClassName() + " cannot cast to " + target_.getClassName());
                        }
                    }
                } else {
                    if (target_argument_types.length != interface_argument_types.length - 1) {
                        throw createMethodWrapper$error(target_method_desc, interface_method_desc, "Parameters length not match.");
                    }
                    for (int i = 0; i < target_argument_types.length; i++) {
                        Type source = interface_argument_types[i + 1];
                        Type target_ = target_argument_types[i];
                        if (getMatchState(source, target_) == MATCH_STATE_CANNOT_SET) {
                            throw createMethodWrapper$error(target_method_desc, interface_method_desc, source.getClassName() + " cannot cast to " + target_.getClassName());
                        }
                    }
                }
            } else {
                target_method_return_type = Type.getMethodType(interface_method_desc);
                if (wrapper_type == MethodWrapperType.STATIC) {
                    target_argument_types = interface_argument_types;
                    target_method_desc = interface_method_desc;
                } else {
                    Type[] nw = new Type[interface_argument_types.length - 1];
                    System.arraycopy(interface_argument_types, 1, nw, 0, nw.length);
                    target_argument_types = nw;
                    target_method_desc = Type.getMethodDescriptor(interface_method_return_type, nw);
                }
            }
        }
        int line0 = 0;
        {
            if (wrapper_type != MethodWrapperType.STATIC) {
                Label line = new Label();
                impl.visitLabel(line);
                impl.visitLineNumber(line0++, line);
                if (interface_argument_types[0].getSort() != Type.OBJECT) {
                    throw createMethodWrapper$error(target_method_desc, interface_method_desc, interface_argument_types[0].getClassName() + " not a object.");
                }
                impl.visitVarInsn(Opcodes.ALOAD, 1);
                if (!interface_argument_types[0].getInternalName().equals(target_internal_source_class_name))
                    impl.visitTypeInsn(Opcodes.CHECKCAST, target_internal_source_class_name);
                sx = 2;
                sw = 1;
            } else {
                sx = 1;
                sw = 0;
            }
        }
/*
        final String target_method_desc;

        if (wrapper_type != MethodWrapperType.STATIC) {
            final Type[] argumentTypes = Type.getArgumentTypes(interface_method_desc);
            target_argument_types = new Type[argumentTypes.length - 1];
            System.arraycopy(argumentTypes, 1, target_argument_types, 0, target_argument_types.length);
            impl.visitVarInsn(Opcodes.ALOAD, 1);
            impl.visitTypeInsn(Opcodes.CHECKCAST, target_internal_source_class_name);
            sx = 2;
        } else {
            target_argument_types = Type.getArgumentTypes(interface_method_desc);
            sx = 1;
        }
        target_method_desc = Type.getMethodDescriptor(Type.getReturnType(interface_method_desc), target_argument_types);
*/

        {
            for (Type type : target_argument_types) {
                Label line = new Label();
                impl.visitLabel(line);
                impl.visitLineNumber(line0++, line);
                switch (type.getSort()) {
                    case Type.DOUBLE:
                        impl.visitVarInsn(Opcodes.DLOAD, sx);
                        break;
                    case Type.FLOAT:
                        impl.visitVarInsn(Opcodes.FLOAD, sx);
                        break;
                    case Type.INT:
                    case Type.SHORT:
                    case Type.BOOLEAN:
                    case Type.BYTE:
                    case Type.CHAR:
                        impl.visitVarInsn(Opcodes.ILOAD, sx);
                        break;
                    case Type.LONG:
                        impl.visitVarInsn(Opcodes.LLOAD, sx);
                        break;
                    case Type.OBJECT:
                    case Type.ARRAY: {
                        impl.visitVarInsn(Opcodes.ALOAD, sx);
                        {
                            Type source = interface_argument_types[sw];
                            if (getMatchState(source, type) == MATCH_STATE_CAN_OVERRIDE) {
                                impl.visitTypeInsn(Opcodes.CHECKCAST, type.getInternalName());
                            }
                        }
                        break;
                    }
                    default:
                        throw new AssertionError();
                }
                sx += type.getSize();
                sw++;
            }
            Label line = new Label();
            impl.visitLabel(line);
            impl.visitLineNumber(777, line);
            impl.visitMethodInsn(
                    wrapper_type == MethodWrapperType.INTERFACE ?
                            Opcodes.INVOKEINTERFACE :
                            (wrapper_type == MethodWrapperType.STATIC ? Opcodes.INVOKESTATIC : Opcodes.INVOKEVIRTUAL),
                    target_internal_source_class_name, target_method_name, target_method_desc,
                    wrapper_type == MethodWrapperType.INTERFACE);
            {
                Type type = Type.getReturnType(target_method_desc);
                switch (type.getSort()) {
                    case Type.DOUBLE:
                        impl.visitInsn(Opcodes.DRETURN);
                        break;
                    case Type.FLOAT:
                        impl.visitInsn(Opcodes.FRETURN);
                        break;
                    case Type.INT:
                    case Type.SHORT:
                    case Type.BOOLEAN:
                    case Type.BYTE:
                    case Type.CHAR:
                        impl.visitInsn(Opcodes.IRETURN);
                        break;
                    case Type.LONG:
                        impl.visitInsn(Opcodes.LRETURN);
                        break;
                    case Type.OBJECT:
                    case Type.ARRAY:
                        impl.visitInsn(Opcodes.ARETURN);
                        break;
                    case Type.VOID:
                        impl.visitInsn(Opcodes.RETURN);
                        break;
                    default:
                        throw new AssertionError();
                }
            }
            impl.visitMaxs(sx, sx + 1);
            impl.visitEnd();
        }
        return cw;
    }
}
