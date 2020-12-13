package io.github.karlatemp.mxlib.maven;

import io.github.karlatemp.mxlib.logger.MLogger;
import org.eclipse.aether.spi.log.Logger;

public class MavenLogger implements Logger {
    private final MLogger logger;

    public MavenLogger(MLogger logger) {
        this.logger = logger;
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(msg);
    }

    @Override
    public void debug(String msg, Throwable error) {
        logger.debug(msg, error);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(msg);
    }

    @Override
    public void warn(String msg, Throwable error) {
        logger.warn(msg, error);
    }
}
