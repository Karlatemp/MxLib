package io.github.karlatmep.mterminal

import io.github.karlatemp.mxlib.common.utils.SimpleClassLocator
import io.github.karlatemp.mxlib.logger.AnsiMessageFactory
import io.github.karlatemp.mxlib.logger.AwesomeLogger
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender
import io.github.karlatemp.mxlib.logger.renders.PrefixedRender.PrefixSupplier
import io.github.karlatemp.mxlib.logger.renders.SimpleRender
import io.github.karlatemp.mxlib.utils.StringUtils.BkColors
import net.mamoe.mirai.console.terminal.ConsoleTerminalExperimentalApi
import net.mamoe.mirai.console.terminal.TerminalUIImpl
import net.mamoe.mirai.utils.MiraiLogger
import java.time.format.DateTimeFormatter
import java.util.function.Consumer

@OptIn(ConsoleTerminalExperimentalApi::class)
object MTerminal : TerminalUIImpl(), Consumer<StringBuilder> {
    val mf: AnsiMessageFactory = AnsiMessageFactory(SimpleClassLocator())
    val renderX = PrefixedRender(
        SimpleRender(mf),
        PrefixSupplier.constant(BkColors._B)
            .plus(PrefixSupplier.dated(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .plus(" " + BkColors._5)
            .plus(PrefixSupplier.dated(DateTimeFormatter.ofPattern("HH:mm:ss")))
            .plus(BkColors.RESET + " [" + BkColors._6)
            .plus(PrefixSupplier.loggerName().aligned(PrefixedRender.AlignedSupplier.AlignType.LEFT))
            .plus(BkColors.RESET + "] [" + BkColors._B)
            .plus(PrefixSupplier.loggerLevel().aligned(PrefixedRender.AlignedSupplier.AlignType.CENTER))
            .plus(BkColors.RESET + "] ")
    )

    override fun newLogger(identity: String?): MiraiLogger {
        return MElLogger(AwesomeLogger.Awesome(identity ?: "TopLevel", this, renderX))
    }

    override fun accept(t: StringBuilder) {
        lineReader.printAbove(t.toString() + ANSI_RESET)
    }
}