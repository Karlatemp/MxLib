package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public interface MLogger {
    String getName();

    boolean isEnabled(MMarket market);

    boolean isEnabled(Level level);

    boolean isEnabled(MMarket market, Level level);

    boolean isDebugEnabled();

    boolean isDebugEnabled(MMarket market);

    boolean isWarnEnabled();

    boolean isWarnEnabled(MMarket market);
    //////////////////////////////////////////////////////////////

    void log(MMarket market, LogRecord logRecord);

    void log(MMarket market, Level level, StringBuilderFormattable message);

    void info(MMarket market, StringBuilderFormattable msg);

    void warn(MMarket market, StringBuilderFormattable msg);

    void error(MMarket market, StringBuilderFormattable msg);

    void debug(MMarket market, StringBuilderFormattable msg);

    void verbose(MMarket market, StringBuilderFormattable msg);

    //////////////////////////////////////////////////////////////

    void log(MMarket market, Level level, StringBuilderFormattable message, Throwable throwable);

    void info(MMarket market, StringBuilderFormattable msg, Throwable throwable);

    void warn(MMarket market, StringBuilderFormattable msg, Throwable throwable);

    void error(MMarket market, StringBuilderFormattable msg, Throwable throwable);

    void debug(MMarket market, StringBuilderFormattable msg, Throwable throwable);

    void verbose(MMarket market, StringBuilderFormattable msg, Throwable throwable);

    //////////////////////////////////////////////////////////////

    void log(LogRecord logRecord);

    void log(Level level, StringBuilderFormattable message);

    void info(StringBuilderFormattable msg);

    void warn(StringBuilderFormattable msg);

    void error(StringBuilderFormattable msg);

    void debug(StringBuilderFormattable msg);

    void verbose(StringBuilderFormattable msg);

    void log(Level level, StringBuilderFormattable message, Throwable throwable);

    void info(StringBuilderFormattable msg, Throwable throwable);

    void warn(StringBuilderFormattable msg, Throwable throwable);

    void error(StringBuilderFormattable msg, Throwable throwable);

    void debug(StringBuilderFormattable msg, Throwable throwable);

    void verbose(StringBuilderFormattable msg, Throwable throwable);

    //////////////////////////////////////////////////////////////

    void info(Throwable throwable);

    void warn(Throwable throwable);

    void error(Throwable throwable);

    void debug(Throwable throwable);

    void verbose(Throwable throwable);

    //////////////////////////////////////////////////////////////

    void log(MMarket market, Level level, String message, Throwable throwable);

    void info(MMarket market, String msg, Throwable throwable);

    void warn(MMarket market, String msg, Throwable throwable);

    void error(MMarket market, String msg, Throwable throwable);

    void debug(MMarket market, String msg, Throwable throwable);

    void verbose(MMarket market, String msg, Throwable throwable);

    //////////////////////////////////////////////////////////////

    void info(MMarket market, Throwable throwable);

    void warn(MMarket market, Throwable throwable);

    void error(MMarket market, Throwable throwable);

    void debug(MMarket market, Throwable throwable);

    void verbose(MMarket market, Throwable throwable);

    //////////////////////////////////////////////////////////////

    void log(Level level, String message);

    void info(String msg);

    void warn(String msg);

    void error(String msg);

    void debug(String msg);

    void verbose(String msg);

    //////////////////////////////////////////////////////////////

    void info(MMarket market, String msg);

    void warn(MMarket market, String msg);

    void error(MMarket market, String msg);

    void debug(MMarket market, String msg);

    void verbose(MMarket market, String msg);

    //////////////////////////////////////////////////////////////


    void log(Level level, String message, Throwable throwable);

    void info(String msg, Throwable throwable);

    void warn(String msg, Throwable throwable);

    void error(String msg, Throwable throwable);

    void debug(String msg, Throwable throwable);

    void verbose(String msg, Throwable throwable);

}
