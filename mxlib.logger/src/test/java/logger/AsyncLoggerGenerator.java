/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger.test/AsyncLoggerGenerator.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package logger;

import io.github.karlatemp.mxlib.logger.MLogger;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

public class AsyncLoggerGenerator {
    private static <T> T getOrNull(T[] arr, int index) {
        if (index < 0 || index >= arr.length) return null;
        return arr[index];
    }

    public static Method[] sortMethods(Method[] methods) {
        return Stream.of(methods)
                .sorted((prev, next) -> {
                    int nameCompare = prev.getName().compareTo(next.getName());
                    if (nameCompare != 0) return nameCompare;
                    Class<?>[] types0 = prev.getParameterTypes();
                    Class<?>[] types1 = next.getParameterTypes();
                    int end = Math.max(types0.length, types1.length);
                    for (int i = 0; i < end; i++) {
                        Class<?> t0 = getOrNull(types0, i);
                        Class<?> t1 = getOrNull(types1, i);
                        if (t0 == null) {
                            return -1;
                        } else if (t1 == null) {
                            return 1;
                        } else {
                            int result = t0.getName().compareTo(t1.getName());
                            if (result != 0) return result;
                        }
                    }
                    return 0;
                })
                .toArray(Method[]::new);
    }

    public static void main(String[] args) throws Throwable {
        Class<MLogger> loggerClass = MLogger.class;
        Class<Executor> executorClass = Executor.class;
        new File("build").mkdir();
        PrintStream ps = new PrintStream("build/AsyncLogger.txt");
        ps.println("/*\n" +
                " * Copyright (c) 2018-" + (Instant.now().atZone(ZoneId.systemDefault()).getYear()) + " Karlatemp. All rights reserved.\n" +
                " * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>\n" +
                " *\n" +
                " * MXLib/MXLib.mxlib-api.main/AsyncLogger.java\n" +
                " *\n" +
                " * Use of this source code is governed by the MIT license that can be found via the following link.\n" +
                " *\n" +
                " * https://github.com/Karlatemp/MxLib/blob/master/LICENSE\n" +
                " */");
        ps.println();
        ps.println("/// The file is automatically generated");
        ps.println();
        ps.println("package io.github.karlatemp.mxlib.logger;");
        ps.println("public class AsyncLogger implements");
        ps.println(loggerClass.getName());
        ps.println(" {");
        ps.println();
        ps.print("private final ");
        ps.print(loggerClass.getName());
        ps.print(" delegate;\nprivate final ");
        ps.print(executorClass.getName());
        ps.println(" executor;");
        ps.println("public AsyncLogger(");
        ps.print(loggerClass.getName());
        ps.println(" delegate, ");
        ps.print(executorClass.getName());
        ps.println(" executor\n) {\nthis.delegate=delegate;\nthis.executor=executor;\n}");

        Method[] methods = sortMethods(loggerClass.getDeclaredMethods());
        for (Method m : methods) {
            if (Modifier.isStatic(m.getModifiers())) continue;
            Class<?>[] types = m.getParameterTypes();

            ps.append("public ").append(m.getReturnType().getCanonicalName());
            ps.append(" ").append(m.getName()).append("(");
            String pps;
            {
                Iterator<Class<?>> iterator = Arrays.asList(types).iterator();
                if (iterator.hasNext()) {
                    ps.append('\n').append(iterator.next().getCanonicalName()).append(" p0");
                    int counter = 1;
                    while (iterator.hasNext()) {
                        ps.append(",\n").append(iterator.next().getCanonicalName());
                        ps.append(" p").print(counter++);
                    }
                    String[] x = new String[types.length];
                    for (int i = 0; i < x.length; i++) {
                        x[i] = "p" + i;
                    }
                    pps = String.join(",", x);
                    ps.append('\n');
                } else {
                    pps = "";
                }
            }
            ps.println(") {");

            if (m.getReturnType() == void.class) {
                ps.print("executor.execute(()->delegate.");
                ps.print(m.getName());
                ps.print("(");
                if (!pps.trim().isEmpty()) {
                    ps.append('\n').println(pps);
                }
                ps.print("));");
            } else {
                ps.print("return delegate." + m.getName() + "(");
                if (!pps.trim().isEmpty()) {
                    ps.append('\n').println(pps);
                }
                ps.println(");");
            }
            ps.println("}");
        }

        ps.println();
        ps.println("}");

        if (ps != System.out) {
            ps.close();
        }
    }
}
