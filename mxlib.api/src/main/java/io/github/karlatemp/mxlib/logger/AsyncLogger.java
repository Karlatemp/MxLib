
/// The file is automatically generated

package io.github.karlatemp.mxlib.logger;

public class AsyncLogger implements
        io.github.karlatemp.mxlib.logger.MLogger {

    public AsyncLogger(
            io.github.karlatemp.mxlib.logger.MLogger
                    delegate,
            java.util.concurrent.Executor
                    executor) {
        this.delegate = delegate;
        this.executor = executor;
    }

    private final
    io.github.karlatemp.mxlib.logger.MLogger
            delegate;
    private final
    java.util.concurrent.Executor
            executor;

    public void warn(io.github.karlatemp.mxlib.utils.StringBuilderFormattable p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.warn(
                p0, p1
        ));
    }

    public void warn(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.String p1) {
        executor.execute(() -> delegate.warn(
                p0, p1
        ));
    }

    public void warn(io.github.karlatemp.mxlib.utils.StringBuilderFormattable p0) {
        executor.execute(() -> delegate.warn(
                p0
        ));
    }

    public void warn(io.github.karlatemp.mxlib.logger.MMarket p0, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p1, java.lang.Throwable p2) {
        executor.execute(() -> delegate.warn(
                p0, p1, p2
        ));
    }

    public void warn(io.github.karlatemp.mxlib.logger.MMarket p0, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p1) {
        executor.execute(() -> delegate.warn(
                p0, p1
        ));
    }

    public void warn(java.lang.String p0) {
        executor.execute(() -> delegate.warn(
                p0
        ));
    }

    public void warn(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.warn(
                p0, p1
        ));
    }

    public void warn(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.String p1, java.lang.Throwable p2) {
        executor.execute(() -> delegate.warn(
                p0, p1, p2
        ));
    }

    public void warn(java.lang.Throwable p0) {
        executor.execute(() -> delegate.warn(
                p0
        ));
    }

    public void warn(java.lang.String p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.warn(
                p0, p1
        ));
    }

    public java.lang.String getName() {
        return delegate.getName(

        );
    }

    public void log(java.util.logging.Level p0, java.lang.String p1, java.lang.Throwable p2) {
        executor.execute(() -> delegate.log(
                p0, p1, p2
        ));
    }

    public void log(io.github.karlatemp.mxlib.logger.MMarket p0, java.util.logging.Level p1, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p2) {
        executor.execute(() -> delegate.log(
                p0, p1, p2
        ));
    }

    public void log(java.util.logging.Level p0, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p1) {
        executor.execute(() -> delegate.log(
                p0, p1
        ));
    }

    public void log(java.util.logging.LogRecord p0) {
        executor.execute(() -> delegate.log(
                p0
        ));
    }

    public void log(java.util.logging.Level p0, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p1, java.lang.Throwable p2) {
        executor.execute(() -> delegate.log(
                p0, p1, p2
        ));
    }

    public void log(io.github.karlatemp.mxlib.logger.MMarket p0, java.util.logging.LogRecord p1) {
        executor.execute(() -> delegate.log(
                p0, p1
        ));
    }

    public void log(io.github.karlatemp.mxlib.logger.MMarket p0, java.util.logging.Level p1, java.lang.String p2, java.lang.Throwable p3) {
        executor.execute(() -> delegate.log(
                p0, p1, p2, p3
        ));
    }

    public void log(java.util.logging.Level p0, java.lang.String p1) {
        executor.execute(() -> delegate.log(
                p0, p1
        ));
    }

    public void log(io.github.karlatemp.mxlib.logger.MMarket p0, java.util.logging.Level p1, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p2, java.lang.Throwable p3) {
        executor.execute(() -> delegate.log(
                p0, p1, p2, p3
        ));
    }

    public void info(java.lang.Throwable p0) {
        executor.execute(() -> delegate.info(
                p0
        ));
    }

    public void info(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.String p1) {
        executor.execute(() -> delegate.info(
                p0, p1
        ));
    }

    public void info(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.String p1, java.lang.Throwable p2) {
        executor.execute(() -> delegate.info(
                p0, p1, p2
        ));
    }

    public void info(java.lang.String p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.info(
                p0, p1
        ));
    }

    public void info(io.github.karlatemp.mxlib.utils.StringBuilderFormattable p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.info(
                p0, p1
        ));
    }

    public void info(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.info(
                p0, p1
        ));
    }

    public void info(java.lang.String p0) {
        executor.execute(() -> delegate.info(
                p0
        ));
    }

    public void info(io.github.karlatemp.mxlib.utils.StringBuilderFormattable p0) {
        executor.execute(() -> delegate.info(
                p0
        ));
    }

    public void info(io.github.karlatemp.mxlib.logger.MMarket p0, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p1) {
        executor.execute(() -> delegate.info(
                p0, p1
        ));
    }

    public void info(io.github.karlatemp.mxlib.logger.MMarket p0, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p1, java.lang.Throwable p2) {
        executor.execute(() -> delegate.info(
                p0, p1, p2
        ));
    }

    public void debug(java.lang.Throwable p0) {
        executor.execute(() -> delegate.debug(
                p0
        ));
    }

    public void debug(io.github.karlatemp.mxlib.utils.StringBuilderFormattable p0) {
        executor.execute(() -> delegate.debug(
                p0
        ));
    }

    public void debug(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.String p1, java.lang.Throwable p2) {
        executor.execute(() -> delegate.debug(
                p0, p1, p2
        ));
    }

    public void debug(java.lang.String p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.debug(
                p0, p1
        ));
    }

    public void debug(java.lang.String p0) {
        executor.execute(() -> delegate.debug(
                p0
        ));
    }

    public void debug(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.debug(
                p0, p1
        ));
    }

    public void debug(io.github.karlatemp.mxlib.logger.MMarket p0, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p1, java.lang.Throwable p2) {
        executor.execute(() -> delegate.debug(
                p0, p1, p2
        ));
    }

    public void debug(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.String p1) {
        executor.execute(() -> delegate.debug(
                p0, p1
        ));
    }

    public void debug(io.github.karlatemp.mxlib.logger.MMarket p0, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p1) {
        executor.execute(() -> delegate.debug(
                p0, p1
        ));
    }

    public void debug(io.github.karlatemp.mxlib.utils.StringBuilderFormattable p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.debug(
                p0, p1
        ));
    }

    public void error(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.error(
                p0, p1
        ));
    }

    public void error(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.String p1) {
        executor.execute(() -> delegate.error(
                p0, p1
        ));
    }

    public void error(io.github.karlatemp.mxlib.logger.MMarket p0, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p1, java.lang.Throwable p2) {
        executor.execute(() -> delegate.error(
                p0, p1, p2
        ));
    }

    public void error(java.lang.String p0) {
        executor.execute(() -> delegate.error(
                p0
        ));
    }

    public void error(io.github.karlatemp.mxlib.logger.MMarket p0, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p1) {
        executor.execute(() -> delegate.error(
                p0, p1
        ));
    }

    public void error(java.lang.Throwable p0) {
        executor.execute(() -> delegate.error(
                p0
        ));
    }

    public void error(java.lang.String p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.error(
                p0, p1
        ));
    }

    public void error(io.github.karlatemp.mxlib.utils.StringBuilderFormattable p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.error(
                p0, p1
        ));
    }

    public void error(io.github.karlatemp.mxlib.utils.StringBuilderFormattable p0) {
        executor.execute(() -> delegate.error(
                p0
        ));
    }

    public void error(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.String p1, java.lang.Throwable p2) {
        executor.execute(() -> delegate.error(
                p0, p1, p2
        ));
    }

    public void verbose(java.lang.String p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.verbose(
                p0, p1
        ));
    }

    public void verbose(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.String p1) {
        executor.execute(() -> delegate.verbose(
                p0, p1
        ));
    }

    public void verbose(java.lang.String p0) {
        executor.execute(() -> delegate.verbose(
                p0
        ));
    }

    public void verbose(io.github.karlatemp.mxlib.utils.StringBuilderFormattable p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.verbose(
                p0, p1
        ));
    }

    public void verbose(io.github.karlatemp.mxlib.utils.StringBuilderFormattable p0) {
        executor.execute(() -> delegate.verbose(
                p0
        ));
    }

    public void verbose(io.github.karlatemp.mxlib.logger.MMarket p0, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p1, java.lang.Throwable p2) {
        executor.execute(() -> delegate.verbose(
                p0, p1, p2
        ));
    }

    public void verbose(io.github.karlatemp.mxlib.logger.MMarket p0, io.github.karlatemp.mxlib.utils.StringBuilderFormattable p1) {
        executor.execute(() -> delegate.verbose(
                p0, p1
        ));
    }

    public void verbose(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.Throwable p1) {
        executor.execute(() -> delegate.verbose(
                p0, p1
        ));
    }

    public void verbose(io.github.karlatemp.mxlib.logger.MMarket p0, java.lang.String p1, java.lang.Throwable p2) {
        executor.execute(() -> delegate.verbose(
                p0, p1, p2
        ));
    }

    public void verbose(java.lang.Throwable p0) {
        executor.execute(() -> delegate.verbose(
                p0
        ));
    }

    public boolean isEnabled(java.util.logging.Level p0) {
        return delegate.isEnabled(
                p0
        );
    }

    public boolean isEnabled(io.github.karlatemp.mxlib.logger.MMarket p0, java.util.logging.Level p1) {
        return delegate.isEnabled(
                p0, p1
        );
    }

    public boolean isEnabled(io.github.karlatemp.mxlib.logger.MMarket p0) {
        return delegate.isEnabled(
                p0
        );
    }

    public boolean isWarnEnabled(io.github.karlatemp.mxlib.logger.MMarket p0) {
        return delegate.isWarnEnabled(
                p0
        );
    }

    public boolean isWarnEnabled() {
        return delegate.isWarnEnabled(

        );
    }

    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled(

        );
    }

    public boolean isDebugEnabled(io.github.karlatemp.mxlib.logger.MMarket p0) {
        return delegate.isDebugEnabled(
                p0
        );
    }
}
