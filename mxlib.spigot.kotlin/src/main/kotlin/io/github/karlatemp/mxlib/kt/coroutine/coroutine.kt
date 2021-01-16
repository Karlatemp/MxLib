/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-kotlin.main/coroutine.kt
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.kt.coroutine

import io.github.karlatemp.mxlib.kt.coroutine.BukkitSchedulerDispatcher.bukkitPlugin
import io.github.karlatemp.mxlib.kt.exception.PlayerQuitException
import io.github.karlatemp.mxlib.kt.runTaskLater
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import kotlin.coroutines.*
import kotlinx.coroutines.runBlocking as runBlockingKC

fun Plugin.newCoroutineScope(
    context: CoroutineContext = EmptyCoroutineContext
): CoroutineScope = CoroutineScope(context + CPlugin(this) + BukkitSchedulerDispatcher)

fun <T> Plugin.runBlocking(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> T
): T = runBlockingKC(context = context + CPlugin(this) + BukkitSchedulerDispatcher, block = block)

fun Plugin.launch(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job = newCoroutineScope(context).launch(start = start, block = block)


@OptIn(ExperimentalStdlibApi::class)
private suspend fun bkDispatcher(): BukkitSchedulerDispatcher {
    val dispatcher = coroutineContext[CoroutineDispatcher]
    if (dispatcher is BukkitSchedulerDispatcher ||
        dispatcher is BukkitSchedulerDispatcher.Wrapper
    ) return BukkitSchedulerDispatcher
    error("Current coroutine dispatcher not BukkitSchedulerDispatcher")
}

suspend fun nextTick(ticks: Long = 1L) {
    bkDispatcher()
    suspendCoroutine<Unit> { continuation ->
        Bukkit.getScheduler().runTaskLater(continuation.context.bukkitPlugin, ticks) {
            continuation.resume(Unit)
        }
    }
}

suspend fun <R> mainThread(block: suspend CoroutineScope.() -> R): R {
    bkDispatcher()
    return withContext(
        bkDispatcher().wrapper() + BukkitSchedulerDispatcher.RunMode.MAIN
    ) { block() }
}

suspend fun <R> asyncThread(block: suspend CoroutineScope.() -> R): R {
    bkDispatcher()
    return withContext(
        bkDispatcher().wrapper() + BukkitSchedulerDispatcher.RunMode.NON_MAIN
    ) { block() }
}

suspend fun Player.waitNextMessage(
    cancelEvent: Boolean = true,
): CompletableDeferred<String> {
    val cd = CompletableDeferred<String>()
    if (!isOnline) {
        return cd.also { it.completeExceptionally(PlayerQuitException()) }
    }
    val plugin = coroutineContext.bukkitPlugin
    val listener = object : Listener {
        @EventHandler
        fun AsyncPlayerChatEvent.handle() {
            if (player != this@waitNextMessage) return
            cd.complete(message)
            if (cancelEvent) isCancelled = true
        }

        @EventHandler
        fun PlayerQuitEvent.handle() {
            if (player != this@waitNextMessage) return
            cd.completeExceptionally(PlayerQuitException())
        }
    }
    cd.invokeOnCompletion { HandlerList.unregisterAll(listener) }
    Bukkit.getPluginManager().registerEvents(listener, plugin)
    return cd
}
