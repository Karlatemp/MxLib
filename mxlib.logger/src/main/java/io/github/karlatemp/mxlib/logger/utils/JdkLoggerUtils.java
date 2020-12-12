package io.github.karlatemp.mxlib.logger.utils;

import io.github.karlatemp.mxlib.utils.Lazy;

import java.util.logging.Logger;

public class JdkLoggerUtils {
    public static final Lazy<Logger> ROOT = Lazy.publication(() -> {
        Logger global = Logger.getGlobal();
        while (true) {
            Logger c = global;
            if ((global = c.getParent()) == null) {
                return c;
            }
        }
    });
}
