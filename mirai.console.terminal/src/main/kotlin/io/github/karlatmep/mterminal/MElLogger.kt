package io.github.karlatmep.mterminal

import io.github.karlatemp.mxlib.logger.MLogger
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase

class MElLogger(val logger: MLogger) : MiraiLoggerPlatformBase() {
    override val identity: String?
        get() = logger.name

    override fun debug0(message: String?, e: Throwable?) {
        logger.debug(message, e)
    }

    override fun error0(message: String?, e: Throwable?) {
        logger.error(message, e)
    }

    override fun info0(message: String?, e: Throwable?) {
        logger.info(message, e)
    }

    override fun verbose0(message: String?, e: Throwable?) {
        logger.verbose(message, e)
    }

    override fun warning0(message: String?, e: Throwable?) {
        logger.warn(message, e)
    }
}