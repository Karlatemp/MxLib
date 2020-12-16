package io.github.karlatemp.mxlib.kt.coroutine

import org.bukkit.plugin.Plugin
import kotlin.coroutines.CoroutineContext

class CPlugin(val plugin: Plugin) : CoroutineContext.Element {
    companion object CKey : CoroutineContext.Key<CPlugin>

    override val key: CoroutineContext.Key<*> get() = CKey
}
