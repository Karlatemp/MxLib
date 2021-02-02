/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger.main/RawMessageFactory.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger;

import io.github.karlatemp.mxlib.annotations.injector.Inject;
import io.github.karlatemp.mxlib.common.utils.PathUtils;
import io.github.karlatemp.mxlib.common.utils.SystemUtils;
import io.github.karlatemp.mxlib.logger.utils.StackTraceElementUtils;
import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.mxlib.utils.ClassLocator;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.lang.invoke.MethodType;
import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.nio.file.Path;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Improved MessageFactory
 */
@SuppressWarnings("unchecked")
public class RawMessageFactory implements MessageFactory {
    protected static final StringBuilderFormattable SUPPRESSED_CAPTION = StringBuilderFormattable.by(
            "Suppressed: "
    ), CAUSE_CAPTION = StringBuilderFormattable.by("Caused by: ");
    // region ThreadInfo helpers
    protected static final @Nullable Predicate<ThreadInfo> IS_DAEMON = Reflections.findMethod(
            ThreadInfo.class,
            "isDaemon",
            false,
            boolean.class
    ).map(method -> Reflections.bindToNoErr(
            Reflections.mapToHandle(method),
            Predicate.class,
            MethodType.methodType(boolean.class, Object.class),
            "test"
    )).orElse(null);
    protected static final @Nullable ToIntFunction<ThreadInfo> GET_PRIORITY = Reflections.findMethod(
            ThreadInfo.class,
            "getPriority",
            false,
            int.class
    ).map(method -> Reflections.bindToNoErr(
            Reflections.mapToHandle(method),
            ToIntFunction.class,
            MethodType.methodType(int.class, Object.class),
            "applyAsInt"
    )).orElse(null);
    // endregion

    @Inject(nullable = true)
    protected ClassLocator locator;

    @Inject
    public RawMessageFactory() {
    }

    public RawMessageFactory(ClassLocator locator) {
        this.locator = locator;
    }

    @Override
    public StringBuilderFormattable getStackTraceElementMessage(StackTraceElement stack) {
        String classname = stack.getClassName();
        String file = null, version = null;
        if (locator != null) {
            Path path = locator.findLocate(classname);
            if (path != null) {
                if (PathUtils.isJre(path)) {
                    file = "JavaRuntime";
                    version = SystemUtils.JAVA_VERSION.get();
                } else {
                    file = path.getFileName().toString();
                    version = findVersion(path);
                }
            }
        }
        return getStackTraceElementMessage$return(stack, file, version);
    }

    protected String findVersion(Path path) {
        if (PathUtils.isOsFileSystem(path)) {
            File file = path.toFile();
            if (file.isFile()) {
                try (ZipFile zip = new ZipFile(file)) {
                    ZipEntry entry = zip.getEntry("/META-INF/MANIFEST.MF");
                    if (entry != null) {
                        Manifest manifest = new Manifest();
                        try (InputStream is = zip.getInputStream(entry)) {
                            manifest.read(is);
                        }
                        Attributes attributes = manifest.getMainAttributes();
                        return attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
                    }
                } catch (Exception ignore) {
                }
            }
        }
        return null;
    }

    @Override
    public StringBuilderFormattable getStackTraceElementMessage$return(
            StackTraceElement stack,
            String file,
            String version
    ) {
        StringBuilderFormattable link = new StringBuilderFormattable.Link();
        {
            boolean append = false;
            String classLoaderName = StackTraceElementUtils.GET_CLASSLOADER_NAME.apply(stack);
            if (classLoaderName != null && !StackTraceElementUtils.DROP_CLASS_LOADER_NAME.test(stack) && !classLoaderName.isEmpty()) {
                link.plusMsg(classLoaderName).plusMsg("/");
                append = true;
            }
            String moduleName = StackTraceElementUtils.GET_MODULE_NAME.apply(stack);
            if (moduleName != null && !moduleName.isEmpty()) {
                link.plusMsg(moduleName);
                append = true;
                String moduleVersion = StackTraceElementUtils.GET_MODULE_VERSION.apply(stack);
                if (moduleVersion != null && !StackTraceElementUtils.DROP_MODULE_VERSION.test(stack) && !moduleVersion.isEmpty()) {
                    link.plusMsg("@").plusMsg(moduleVersion);
                }
            }
            if (append) link.plusMsg("/");
            link.plusMsg(stack.getClassName());
            link.plusMsg(".");
            link.plusMsg(stack.getMethodName());
            if (stack.isNativeMethod()) {
                link.plusMsg("(Native Method)");
            } else {
                int linenum = stack.getLineNumber();
                String filename = stack.getFileName();
                if (filename != null) {
                    link.plusMsg("(").plusMsg(filename);
                    if (linenum >= 0) {
                        link.plusMsg(":").plusMsg(linenum);
                    }
                    link.plusMsg(")");
                } else {
                    link.plusMsg("(Unknown Source)");
                }
            }
        }
        link.plusMsg(" [")
                .plusMsg(file == null ? "?" : file)
                .plusMsg(":")
                .plusMsg(version == null ? "?" : version)
                .plusMsg("]");
        return link;
    }

    protected void plusLockInfo(StringBuilderFormattable link, LockInfo lockInfo) {
        link.plusMsg(lockInfo);
    }

    @Override
    public StringBuilderFormattable dump(ThreadInfo inf, int frames) {
        boolean full = frames < 0;
        StringBuilderFormattable.Link link = new StringBuilderFormattable.Link();
        link.plusMsg("\"").plusMsg(inf.getThreadName()).plusMsg("\"");
        if (IS_DAEMON != null && IS_DAEMON.test(inf)) {
            link.plusMsg(" daemon");
        }
        if (GET_PRIORITY != null) {
            link.plusMsg(" prio=").plusMsg(GET_PRIORITY.applyAsInt(inf));
        }
        link.plusMsg(" Id=").plusMsg(inf.getThreadId()).plusMsg(" ").plusMsg(inf.getThreadState());
        if (inf.getLockName() != null) {
            link.plusMsg(" on ").plusMsg(inf.getLockName());
        }
        if (inf.getLockOwnerName() != null) {
            link.plusMsg(" owned by \"").plusMsg(inf.getLockOwnerName())
                    .plusMsg("\" Id=").plusMsg(inf.getLockOwnerId());
        }
        if (inf.isSuspended()) {
            link.plusMsg(" (suspended)");
        }
        if (inf.isInNative()) {
            link.plusMsg(" (in native)");
        }
        link.plusMsg(StringBuilderFormattable.LN);
        StackTraceElement[] stackTrace = inf.getStackTrace();
        int i = 0;
        for (; i < stackTrace.length && (full || i < frames); i++) {
            StackTraceElement ste = stackTrace[i];
            plusStackTraceElement(link, ste);
            link.plusMsg(StringBuilderFormattable.LN);
            if (i == 0 && inf.getLockInfo() != null) {
                Thread.State ts = inf.getThreadState();
                switch (ts) {
                    case BLOCKED:
                        link.plusMsg("\t-  blocked on ");
                        plusLockInfo(link, inf.getLockInfo());
                        link.plusMsg(StringBuilderFormattable.LN);
                        break;
                    case WAITING:
                    case TIMED_WAITING:
                        link.plusMsg("\t-  waiting on ");
                        plusLockInfo(link, inf.getLockInfo());
                        link.plusMsg(StringBuilderFormattable.LN);
                        break;
                    default:
                }
            }

            for (MonitorInfo mi : inf.getLockedMonitors()) {
                if (mi.getLockedStackDepth() == i) {
                    link.plusMsg("\t-  locked ").plusMsg(mi);
                    link.plusMsg(StringBuilderFormattable.LN);
                }
            }
        }
        if (i < stackTrace.length) {
            link.plusMsg("\t...");
            link.plusMsg(StringBuilderFormattable.LN);
        }

        LockInfo[] locks = inf.getLockedSynchronizers();
        if (locks.length > 0) {
            link.plusMsg("\n\tNumber of locked synchronizers = " + locks.length);
            link.plusMsg(StringBuilderFormattable.LN);
            for (LockInfo li : locks) {
                link.plusMsg("\t- ");
                plusLockInfo(link, li);
                link.plusMsg(StringBuilderFormattable.LN);
            }
        }
        return link;
    }

    @Override
    public StringBuilderFormattable CIRCULAR_REFERENCE(Throwable throwable) {
        StringBuilderFormattable link = StringBuilderFormattable.by("\t[CIRCULAR REFERENCE:").asLink();
        plusThrowable(link, throwable);
        return link.plusMsg("]");
    }

    @Override
    public StringBuilderFormattable dump(Throwable thr, boolean hasFrames) {
        return printStackTrace(thr, hasFrames);
    }

    protected StringBuilderFormattable SUPPRESSED_CAPTION() {
        return SUPPRESSED_CAPTION;
    }

    protected StringBuilderFormattable CAUSE_CAPTION() {
        return CAUSE_CAPTION;
    }

    protected void plusThrowable(StringBuilderFormattable link, Throwable s) {
        link.plusMsg(s);
    }

    protected void plusStackTraceElement(StringBuilderFormattable link, StackTraceElement s) {
        link.plusMsg("\tat ").plusMsg(s);
    }

    protected StringBuilderFormattable printStackTrace(Throwable s, boolean frames) {
        StringBuilderFormattable link = new StringBuilderFormattable.Link();
        // Guard against malicious overrides of Throwable.equals by
        // using a Set with identity equality semantics.
        Set<Throwable> dejaVu = Collections.newSetFromMap(new IdentityHashMap<>());
        dejaVu.add(s);

        // Print our stack trace
        plusThrowable(link, s);
        link.plusMsg(StringBuilderFormattable.LN);
        StackTraceElement[] trace = frames ? s.getStackTrace() : null;
        if (frames) {
            for (StackTraceElement traceElement : trace) {
                plusStackTraceElement(link, traceElement);
                link.plusMsg(StringBuilderFormattable.LN);
            }
        }
        // Print suppressed exceptions, if any
        for (Throwable se : s.getSuppressed())
            printEnclosedStackTrace(se, link, trace, SUPPRESSED_CAPTION(), StringBuilderFormattable.LT, dejaVu, frames);

        // Print cause, if any
        Throwable ourCause = s.getCause();
        if (ourCause != null)
            printEnclosedStackTrace(ourCause, link, trace, CAUSE_CAPTION(), StringBuilderFormattable.EMPTY, dejaVu, frames);
        return link;
    }

    protected void plusFramesInCommon(StringBuilderFormattable link, int framesInCommon) {
        link.plusMsg("\t... ").plusMsg(framesInCommon).plusMsg(" more");
    }

    /**
     * Print our stack trace as an enclosed exception for the specified
     * stack trace.
     */
    protected void printEnclosedStackTrace(Throwable thr,
                                           StringBuilderFormattable link,
                                           StackTraceElement[] enclosingTrace,
                                           StringBuilderFormattable caption,
                                           StringBuilderFormattable prefix,
                                           Set<Throwable> dejaVu,
                                           boolean frames) {
        if (dejaVu.contains(thr)) {
            link.plusMsg(CIRCULAR_REFERENCE(thr)).plusMsg(StringBuilderFormattable.LN);
        } else {
            dejaVu.add(thr);
            StackTraceElement[] trace = frames ? thr.getStackTrace() : null;
            if (frames) {
                // Compute number of frames in common between this and enclosing trace
                int m = trace.length - 1;
                int n = enclosingTrace.length - 1;
                while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n])) {
                    m--;
                    n--;
                }
                int framesInCommon = trace.length - 1 - m;

                // Print our stack trace
                link.plusMsg(prefix).plusMsg(caption);
                plusThrowable(link, thr);
                link.plusMsg(StringBuilderFormattable.LN);
                for (int i = 0; i <= m; i++) {
                    link.plusMsg(prefix);
                    plusStackTraceElement(link, trace[i]);
                    link.plusMsg(StringBuilderFormattable.LN);
                }
                if (framesInCommon != 0) {
                    link.plusMsg(prefix);
                    plusFramesInCommon(link, framesInCommon);
                    link.plusMsg(StringBuilderFormattable.LN);
                }
            }
            // Print suppressed exceptions, if any
            for (Throwable se : thr.getSuppressed()) {
                printEnclosedStackTrace(se, link, trace, SUPPRESSED_CAPTION,
                        prefix.copy().plusMsg(StringBuilderFormattable.LT), dejaVu, frames);
            }
            // Print cause, if any
            Throwable ourCause = thr.getCause();
            if (ourCause != null)
                printEnclosedStackTrace(ourCause, link, trace, CAUSE_CAPTION, prefix, dejaVu, frames);
        }
    }

}
