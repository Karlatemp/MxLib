@file:JvmName("BeanKt")

package io.github.karlatemp.mxlib.bean

import java.util.*
import java.util.stream.Stream

public inline fun <reified T> IBeanManager.getBy(): Optional<T> = getBy(T::class.java)

public inline fun <reified T> IBeanManager.getBy(name: String): Optional<T> = getBy(T::class.java, name)

public inline fun <reified T> IBeanManager.getAllOf(): Stream<T> = getAll(T::class.java)
