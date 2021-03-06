/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common-kotlin.main/Reflection.kt
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

@file:JvmName("ReflectionKt")
@file:Suppress("NOTHING_TO_INLINE")

package io.github.karlatemp.mxlib.reflect

import java.lang.reflect.*
import java.util.*
import java.util.stream.Stream
import kotlin.reflect.KClass

public inline fun <reified T> Class<T>.allocate(): T = Reflections.allocObject(this)
public inline fun <reified T : Any> KClass<T>.allocate(): T = Reflections.allocObject(this.java)

public inline fun Class<*>.findMethod(
        name: String,
        isStatic: Boolean,
        returnType: Class<*>? = null,
        parameters: Array<Class<*>>? = null
): Optional<Method> = Reflections.`findMethod$kt`(this, name, isStatic, returnType, parameters)

public inline fun <T : AccessibleObject> T.trust(): T = apply { Reflections.openAccess<T>().accept(this) }

public inline fun Class<*>.allMembers(): Stream<Member> = Reflections.allMembers(this)
public inline fun Class<*>.allFields(): Stream<Field> = Reflections.allFields(this)
public inline fun Class<*>.allMethods(): Stream<Method> = Reflections.allMethods(this)

public inline fun <T : Member> Stream<T>.statics(): Stream<T> = filter(Reflections.ModifierFilter.STATIC)
public inline fun <T : Member> Stream<T>.nonStatics(): Stream<T> = filter(Reflections.ModifierFilter.NON_STATIC)
public inline fun <T : AnnotatedElement, reified A : Annotation> Stream<T>.annotated(): Stream<T> = filter(Reflections.withAnnotated(A::class.java))
public inline fun <T : AccessibleObject> Stream<T>.trust(): Stream<T> = peek(Reflections.openAccess())
