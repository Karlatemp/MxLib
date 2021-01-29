/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-kotlin.main/schedulers.kt
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

@file:Suppress("NOTHING_TO_INLINE")

package io.github.karlatemp.mxlib.kt

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask

public inline fun BukkitScheduler.runTaskLater(
    plugin: Plugin,
    ticks: Long,
    block: Runnable
): BukkitTask = runTaskLater(plugin, block, ticks)

public inline fun BukkitScheduler.runTaskTimer(
    plugin: Plugin,
    delay: Long,
    period: Long,
    block: Runnable
): BukkitTask = runTaskTimer(plugin, block, delay, period)
