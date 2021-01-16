/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api-kotlin.main/cast.kt
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

@file:Suppress("NOTHING_TO_INLINE")

package io.github.karlatemp.mxlib.utils

import java.util.*

public inline fun <reified T> Any?.castTo(): T = this as T
public inline fun <reified T> Any?.safeCast(): T? = this as? T
public inline fun <reified T> T?.requireNonNull(): T = this ?: error("null cannot cast to ${T::class.qualifiedName}")
public inline fun <T> T?.requireNonNull(msg: String): T = this ?: error(msg)
public inline val <T> Optional<T>.valueOrNull: T? get() = orElse(null)

