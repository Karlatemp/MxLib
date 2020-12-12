package io.github.karlatemp.mxlib;

import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.exception.ValueInitializedException;
import io.github.karlatemp.mxlib.exception.ValueNotInitializedException;
import io.github.karlatemp.mxlib.logger.MLogger;
import io.github.karlatemp.mxlib.logger.MLoggerFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class MxLib {
    private static final AtomicReference<IBeanManager> BEAN_MANAGER_ATOMIC_REFERENCE = new AtomicReference<>();
    private static final AtomicReference<MLogger> LOGGER_ATOMIC_REFERENCE = new AtomicReference<>();
    private static final AtomicReference<Logger> JDK_LOGGER_ATOMIC_REFERENCE = new AtomicReference<>();
    private static final AtomicReference<MLoggerFactory> LOGGER_FACTORY_ATOMIC_REFERENCE = new AtomicReference<>();
    private static final AtomicReference<File> DATA_STORAGE_ATOMIC_REFERENCE = new AtomicReference<>();

    @Contract(pure = true)
    public static @NotNull IBeanManager getBeanManager() {
        IBeanManager beanManager = BEAN_MANAGER_ATOMIC_REFERENCE.get();
        if (beanManager == null) throw new ValueNotInitializedException();
        return beanManager;
    }

    @Contract(pure = true)
    public static @NotNull MLogger getLogger() {
        MLogger logger = LOGGER_ATOMIC_REFERENCE.get();
        if (logger == null) throw new ValueNotInitializedException();
        return logger;
    }

    @Contract(pure = true)
    public static @NotNull Logger getJdkLogger() {
        Logger logger = JDK_LOGGER_ATOMIC_REFERENCE.get();
        if (logger == null) throw new ValueNotInitializedException();
        return logger;
    }

    public static void setBeanManager(@NotNull IBeanManager beanManager) {
        if (!BEAN_MANAGER_ATOMIC_REFERENCE.compareAndSet(null, beanManager)) {
            throw new ValueInitializedException();
        }
    }

    public static void setLogger(@NotNull MLogger logger) {
        if (!LOGGER_ATOMIC_REFERENCE.compareAndSet(null, logger)) {
            throw new ValueInitializedException();
        }
    }

    @Contract(pure = true)
    public static MLoggerFactory getLoggerFactory() {
        MLoggerFactory factory = LOGGER_FACTORY_ATOMIC_REFERENCE.get();
        if (factory == null) throw new ValueNotInitializedException();
        return factory;
    }

    public static void setLoggerFactory(@NotNull MLoggerFactory factory) {
        if (!LOGGER_FACTORY_ATOMIC_REFERENCE.compareAndSet(null, factory)) {
            throw new ValueInitializedException();
        }
    }

    public static void setJdkLogger(@NotNull Logger logger) {
        if (!JDK_LOGGER_ATOMIC_REFERENCE.compareAndSet(null, logger)) {
            throw new ValueInitializedException();
        }
    }

    @Contract(pure = true)
    public static boolean isBeanManagerInitialized() {
        return !BEAN_MANAGER_ATOMIC_REFERENCE.compareAndSet(null, null);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Contract(pure = true)
    public static File getDataStorage() {
        AtomicReference reference = DATA_STORAGE_ATOMIC_REFERENCE;
        while (true) {
            Object o = reference.get();
            if (o == null) {
                if (reference.compareAndSet(null, reference)) {
                    // Initializing
                    try {
                        reference.set(new File(System.getProperty("mxlib.data.home", "MxLibData")).getAbsoluteFile());
                    } catch (Throwable any) {
                        reference.set(any);
                    }
                }
            } else if (o instanceof File) {
                return (File) o;
            } else if (o instanceof RuntimeException) {
                throw (RuntimeException) o;
            } else if (o instanceof Error) {
                throw (Error) o;
            } else if (o instanceof Throwable) {
                throw new RuntimeException((Throwable) o);
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void setDataStorage(@NotNull File dir) {
        AtomicReference reference = DATA_STORAGE_ATOMIC_REFERENCE;
        while (true) {
            Object obj = reference.get();
            if (obj == null || obj instanceof Throwable) {
                if (reference.compareAndSet(obj, dir)) {
                    return;
                }
                continue;
            }
            throw new ValueInitializedException();
        }
    }
}
