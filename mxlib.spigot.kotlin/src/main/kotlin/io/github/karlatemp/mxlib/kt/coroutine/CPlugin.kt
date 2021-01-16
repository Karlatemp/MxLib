/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-kotlin.main/CPlugin.kt
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.kt.coroutine

import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

class CPlugin(val plugin: Plugin) : CoroutineContext.Element {
    companion object CKey : CoroutineContext.Key<CPlugin>

    override val key: CoroutineContext.Key<*> get() = CKey
}
