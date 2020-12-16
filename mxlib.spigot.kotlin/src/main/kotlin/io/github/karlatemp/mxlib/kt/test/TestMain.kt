package io.github.karlatemp.mxlib.kt.test

import io.github.karlatemp.mxlib.kt.coroutine.asyncThread
import io.github.karlatemp.mxlib.kt.coroutine.launch
import io.github.karlatemp.mxlib.kt.coroutine.mainThread
import io.github.karlatemp.mxlib.kt.coroutine.nextTick
import io.github.karlatemp.mxlib.spigot.MinecraftServerHelper
import kotlinx.coroutines.delay
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@PublishedApi
internal object TestMain {
    private val plugin = JavaPlugin.getProvidingPlugin(TestMain::class.java)

    @JvmStatic
    @PublishedApi
    internal fun handle(
        player: Player
    ) {
        fun msg(msg: String) {
            player.sendMessage("[MxLib] [${Thread.currentThread().name}] [${MinecraftServerHelper.getCurrentTick()}] $msg")
        }
        plugin.launch {
            msg("Launched")
            delay(1000)
            msg("Delay... Delay...!")
            nextTick()
            msg("OK, Now next Tick! Invoked!")
            asyncThread {
                msg("Async Thread!!!!!!!!")
                delay(1000)
                msg("Async!")
                mainThread { msg("Back to Main Thread") }
            }
            msg("Done!")
        }
    }
}
