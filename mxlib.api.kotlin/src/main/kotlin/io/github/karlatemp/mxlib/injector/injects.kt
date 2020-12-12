@file:JvmName("InjectKt")
@file:Suppress("NOTHING_TO_INLINE")

package io.github.karlatemp.mxlib.injector

import kotlin.reflect.KProperty

public inline operator fun <T> Injected.Nillable<T>.getValue(thiz: Any?, p: KProperty<*>): T? = getValue()

public inline operator fun <T> Injected.Nonnull<T>.getValue(thiz: Any?, p: KProperty<*>): T = getValue()
