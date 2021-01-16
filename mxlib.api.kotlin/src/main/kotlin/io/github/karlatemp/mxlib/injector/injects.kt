/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api-kotlin.main/injects.kt
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

@file:JvmName("InjectKt")
@file:Suppress("NOTHING_TO_INLINE")

package io.github.karlatemp.mxlib.injector

import kotlin.reflect.KProperty

public inline operator fun <T> Injected.Nillable<T>.getValue(thiz: Any?, p: KProperty<*>): T? = getValue()

public inline operator fun <T> Injected.Nonnull<T>.getValue(thiz: Any?, p: KProperty<*>): T = getValue()
