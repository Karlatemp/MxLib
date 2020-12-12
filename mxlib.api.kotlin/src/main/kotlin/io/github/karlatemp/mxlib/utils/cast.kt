@file:Suppress("NOTHING_TO_INLINE")

package io.github.karlatemp.mxlib.utils

import java.util.*

public inline fun <reified T> Any?.castTo(): T = this as T
public inline fun <reified T> Any?.safeCast(): T? = this as? T
public inline fun <reified T> T?.requireNonNull(): T = this ?: error("null cannot cast to ${T::class.qualifiedName}")
public inline fun <T> T?.requireNonNull(msg: String): T = this ?: error(msg)
public inline val <T> Optional<T>.valueOrNull: T? get() = orElse(null)

