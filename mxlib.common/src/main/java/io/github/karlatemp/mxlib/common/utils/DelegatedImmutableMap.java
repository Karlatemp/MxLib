package io.github.karlatemp.mxlib.common.utils;

import com.google.common.collect.ImmutableMap;
import io.github.karlatemp.unsafeaccessor.Root;
import io.github.karlatemp.unsafeaccessor.Unsafe;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

public class DelegatedImmutableMap implements Opcodes {
    private static final Type TYPE_ImmutableMap = Type.getType(ImmutableMap.class);
    private static final Class<?> IMPL_CLASS;
    private static final Unsafe UNSAFE = Root.getUnsafe();
    private static final long IMPL_CLASS$delegate$offset;

    private static byte[] generationByteCode() {
        ClassWriter writer = new ClassWriter(0);
        String typeName = "io/github/karlatemp/mxlib/common/utils/DelegatedImmutableMap$Implement$" + System.currentTimeMillis();
        writer.visit(V1_8, ACC_PUBLIC,
                typeName, null, TYPE_ImmutableMap.getInternalName(), null
        );
        writer.visitField(ACC_PRIVATE, "delegate", "Ljava/util/Map;", null, null);
        for (Method method : Map.class.getMethods()) {
            if (Modifier.isStatic(method.getModifiers())) continue;
            try {
                if (Modifier.isFinal(
                        ImmutableMap.class.getMethod(
                                method.getName(), method.getParameterTypes()
                        ).getModifiers()
                )) continue;
            } catch (Throwable ignored) {
            }
            if (method.getName().equals("equals")) continue;
            if (method.getName().equals("hashCode")) continue;
            MethodVisitor methodVisitor = writer.visitMethod(ACC_PUBLIC, method.getName(), Type.getMethodDescriptor(method), null, null);
            methodVisitor.visitCode();
            methodVisitor.visitVarInsn(ALOAD, 0);
            methodVisitor.visitFieldInsn(GETFIELD, typeName, "delegate", "Ljava/util/Map;");
            Type returnType = Type.getReturnType(method);
            Type[] arguments = Type.getArgumentTypes(method);
            int slot = 1;
            for (Type arg : arguments) {
                methodVisitor.visitVarInsn(arg.getOpcode(ILOAD), slot);
                slot += arg.getSize();
            }
            methodVisitor.visitMethodInsn(INVOKEINTERFACE, "java/util/Map", method.getName(), Type.getMethodDescriptor(method), true);
            methodVisitor.visitInsn(returnType.getOpcode(IRETURN));
            methodVisitor.visitMaxs(slot, slot);
        }
        return writer.toByteArray();
    }

    static {
        byte[] bytecode = generationByteCode();
        IMPL_CLASS = UNSAFE.defineClass(null, bytecode, 0, bytecode.length, DelegatedImmutableMap.class.getClassLoader(), null);
        IMPL_CLASS$delegate$offset = UNSAFE.objectFieldOffset(IMPL_CLASS, "delegate");
    }

    @SuppressWarnings("unchecked")
    public static <K, V> ImmutableMap<K, V> newDelegatedImmutableMap(
            @NotNull Map<K, V> delegate
    ) {
        try {
            Object instance = UNSAFE.allocateInstance(IMPL_CLASS);
            UNSAFE.putReference(instance, IMPL_CLASS$delegate$offset, delegate);
            return (ImmutableMap<K, V>) instance;
        } catch (InstantiationException e) {
            UNSAFE.throwException(e);
            throw new AssertionError(e);
        }
    }
}
