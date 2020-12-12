package io.github.karlatemp.mxlib.logger;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MJdkLoggerHandler extends Handler {
    private final MLogger logger;

    public MJdkLoggerHandler(MLogger logger) {
        this.logger = logger;
    }

    @Override
    public void publish(LogRecord record) {
        logger.log(record);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
}
