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
