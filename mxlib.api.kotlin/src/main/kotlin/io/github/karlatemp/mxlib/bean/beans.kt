/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api-kotlin.main/beans.kt
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

@file:JvmName("BeanKt")

package io.github.karlatemp.mxlib.bean

import java.util.*
import java.util.stream.Stream

public inline fun <reified T> IBeanManager.getBy(): Optional<T> = getBy(T::class.java)

public inline fun <reified T> IBeanManager.getBy(name: String): Optional<T> = getBy(T::class.java, name)

public inline fun <reified T> IBeanManager.getAllOf(): Stream<T> = getAll(T::class.java)
