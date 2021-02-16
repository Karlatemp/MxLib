/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common.main/LambdaFactory.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.reflect;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.MethodInfo;
import io.github.karlatemp.mxlib.classmodel.analysis.AbstractMethodAnalyzer;
import io.github.karlatemp.mxlib.classmodel.resolve.Resolver;
import io.github.karlatemp.unsafeaccessor.Root;
import io.github.karlatemp.unsafeaccessor.Unsafe;
import org.jetbrains.annotations.ApiStatus;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.invoke.*;

@ApiStatus.Experimental
public class LambdaFactory {
    public static <T> T bridge(
            Class<T> interfaceClass,
            MethodHandles.Lookup actor,
            MethodHandle handle
    ) {
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException(interfaceClass + " not a interface");
        }
        if ((actor.lookupModes() & MethodHandles.Lookup.PRIVATE) == 0) {
            throw new IllegalArgumentException(new IllegalAccessException("Actor don't have private access permission"));
        }

        try {
            if (
                    Class.forName(interfaceClass.getName(), false, actor.lookupClass().getClassLoader()) != interfaceClass
            ) {
                throw new ClassNotFoundException(interfaceClass.getName());
            }
        } catch (ClassNotFoundException classNotFoundException) {
            throw new IllegalArgumentException(new IllegalAccessException("Actor " + actor.lookupClass() + " cannot access " + interfaceClass));
        }

        MethodHandleInfo handleInfo;
        try {
            handleInfo = actor.revealDirect(handle);
        } catch (IllegalArgumentException ignored) {
            handleInfo = null;
        }

        MethodInfo singleAbstractMethod;
        try {
            singleAbstractMethod = AbstractMethodAnalyzer.findSingleAbstractMethod(
                    AbstractMethodAnalyzer.findAbstractMethods(
                            ClassInfo.ofClass(interfaceClass).getMethods()
                    )
            );
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
        MethodType methodType;
        try {
            methodType = Resolver.DEFAULT.withLoader(actor.lookupClass().getClassLoader()).resolveMethodType(singleAbstractMethod);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new IllegalArgumentException(new IllegalAccessException().initCause(classNotFoundException));
        }
        handle.asType(methodType);
        if (handleInfo != null) {
            CallSite callSite;
            try {
                callSite = LambdaMetafactory.metafactory(
                        actor,
                        singleAbstractMethod.getName(),
                        MethodType.methodType(interfaceClass),
                        methodType,
                        handle,
                        methodType
                );
            } catch (LambdaConversionException e) {
                throw new IllegalArgumentException(e);
            }
            try {
                return interfaceClass.cast(callSite.getTarget().invoke());
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }

        ClassWriter classNode = new ClassWriter(0);
        String classname = (actor.lookupClass().getName() + "$$MxLambda$$" + System.currentTimeMillis()).replace('.', '/');

        classNode.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, classname, null, "java/lang/Object", new String[]{interfaceClass.getName().replace('.', '/')});
        classNode.visitField(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC, "handle", "Ljava/lang/invoke/MethodHandle;", null, null);

        String desc = methodType.toMethodDescriptorString();
        WrappedClassImplements.publicObjectConstructor(classNode, "java/lang/Object");

        MethodVisitor method = classNode.visitMethod(Opcodes.ACC_PUBLIC, singleAbstractMethod.getName(), desc, null, null);
        method.visitAnnotation("Ljdk/internal/vm/annotation/Hidden;", true);
        method.visitAnnotation("Ljava/lang/invoke/LambdaForm$Hidden;", true);

        method.visitFieldInsn(Opcodes.GETSTATIC, classname, "handle", "Ljava/lang/invoke/MethodHandle;");
        int counter = 1;
        for (Type argType : Type.getArgumentTypes(desc)) {
            method.visitVarInsn(argType.getOpcode(Opcodes.ILOAD), counter);
            counter += argType.getSize();
        }
        method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/invoke/MethodHandle", "invoke", desc, false);

        method.visitInsn(Type.getReturnType(desc).getOpcode(Opcodes.IRETURN));
        method.visitMaxs(counter, counter);

        byte[] code = classNode.toByteArray();
        Class<?> anonymousClass = Unsafe.getUnsafe().defineAnonymousClass(actor.lookupClass(), code, null);

        try {
            Root.openAccess(anonymousClass.getDeclaredField("handle")).set(null, handle);

            return interfaceClass.cast(anonymousClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
