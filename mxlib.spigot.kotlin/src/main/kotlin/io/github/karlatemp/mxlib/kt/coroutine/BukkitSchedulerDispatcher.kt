package io.github.karlatemp.mxlib.kt.coroutine

import io.github.karlatemp.mxlib.kt.runTaskLater
import kotlinx.coroutines.*
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@OptIn(InternalCoroutinesApi::class)
object BukkitSchedulerDispatcher : CoroutineDispatcher(), Delay {
    internal fun wrapper(): Wrapper = Wrapper()

    internal class Wrapper : CoroutineDispatcher(), Delay {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
            BukkitSchedulerDispatcher.dispatch(context, block)
        }

        override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
            BukkitSchedulerDispatcher.scheduleResumeAfterDelay(timeMillis, continuation)
        }
    }

    private val scheduler = Bukkit.getScheduler()
    internal val CoroutineContext.bukkitPlugin: Plugin
        get() = get(CPlugin)?.plugin ?: error("Missing Plugin in CoroutineContext")

    internal enum class RunMode : CoroutineContext.Element {
        MAIN {
            override fun run(context: CoroutineContext, block: Runnable) {
                if (Bukkit.isPrimaryThread()) {
                    block.run()
                } else {
                    scheduler.runTask(context.bukkitPlugin, block)
                }
            }
        },
        NON_MAIN {
            override fun run(context: CoroutineContext, block: Runnable) {
                if (Bukkit.isPrimaryThread()) {
                    scheduler.runTaskAsynchronously(context.bukkitPlugin, block)
                } else {
                    block.run()
                }
            }
        },
        ;

        companion object RKey : CoroutineContext.Key<RunMode>

        override val key: CoroutineContext.Key<*> get() = RKey
        abstract fun run(context: CoroutineContext, block: Runnable)
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        (context[RunMode] ?: RunMode.MAIN).run(context, block)
    }

    override fun scheduleResumeAfterDelay(timeMillis: Long, continuation: CancellableContinuation<Unit>) {
        val ticks = timeMillis / 50
        kotlin.runCatching {
            scheduler.runTaskLater(continuation.context.bukkitPlugin, ticks) {
                continuation.resume(Unit)
            }
        }.onFailure {
            continuation.resumeWithException(it)
        }
    }
}
