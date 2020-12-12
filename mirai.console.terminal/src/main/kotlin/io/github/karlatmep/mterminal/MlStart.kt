package io.github.karlatmep.mterminal

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.console.MiraiConsole
import net.mamoe.mirai.console.terminal.MiraiConsoleImplementationTerminal
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

object MlStart {
    @OptIn(ConsoleExperimentalApi::class)
    @JvmStatic
    fun main(args: Array<String>) {
        MiraiConsoleTerminalLoader.startAsDaemon(
            MiraiConsoleImplementationTerminal(terminalUi = MTerminal)
        )
        try {
            runBlocking {
                MiraiConsole.job.join()
            }
        } catch (e: CancellationException) {
            // ignored
        }
    }
}
