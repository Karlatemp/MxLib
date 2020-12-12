package io.github.karlatemp.mxlib.common.utils;

import io.github.karlatemp.mxlib.utils.Lazy;

public class SystemUtils {
    public static final Lazy<String> JAVA_VERSION = Lazy.publication(() -> System.getProperty("java.version"));
}
