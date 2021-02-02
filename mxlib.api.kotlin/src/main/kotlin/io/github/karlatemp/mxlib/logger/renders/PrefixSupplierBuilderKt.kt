/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api-kotlin.main/PrefixSupplierBuilderKt.kt
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

@file:Suppress("NOTHING_TO_INLINE")
@file:JvmName("PrefixSupplierBuilderKotlin")

package io.github.karlatemp.mxlib.logger.renders

import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public open class PrefixSupplierBuilderKt : PrefixSupplierBuilder {
    public constructor() : super()
    public constructor(supplier: PrefixedRender.PrefixSupplier) : super(supplier)

    public inline operator fun CharSequence.unaryPlus() {
        append(this)
    }

    public inline operator fun PrefixedRender.PrefixSupplier.unaryPlus() {
        append(this)
    }
}

/**
 * DSL for building [PrefixedRender.PrefixSupplier]
 *
 * ```kotlin
 * buildPrefixSupplier(PrefixSupplier.constant(StringUtils.BkColors._B)) {
 *      +PrefixSupplier.dated(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
 *      +(" " + StringUtils.BkColors._5)
 *      +PrefixSupplier.dated(DateTimeFormatter.ofPattern("HH:mm:ss"))
 *      +(StringUtils.BkColors.RESET + " [" + StringUtils.BkColors._6)
 *      +PrefixSupplier.loggerAndRecordName().aligned(PrefixedRender.AlignedSupplier.AlignType.LEFT)
 *      +(StringUtils.BkColors.RESET + "] [" + StringUtils.BkColors._B)
 *      +PrefixSupplier.loggerLevel().aligned(PrefixedRender.AlignedSupplier.AlignType.CENTER)
 *      +(StringUtils.BkColors.RESET + "] ")
 * }
 * ```
 */
public inline fun buildPrefixSupplier(
    current: PrefixedRender.PrefixSupplier = PrefixedRender.PrefixSupplier.EMPTY,
    block: PrefixSupplierBuilderKt.() -> Unit,
): PrefixedRender.PrefixSupplier {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return PrefixSupplierBuilderKt(current).apply(block).complete()
}
