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
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Executor;

public class AsyncLoggerGenerator {

    public static void main(String[] args) throws Throwable {
        Class<MLogger> loggerClass = MLogger.class;
        Class<Executor> executorClass = Executor.class;
        new File("build").mkdir();
        PrintStream ps = new PrintStream("build/AsyncLogger.txt");
        ps.println();
        ps.println("/// The file is automatically generated");
        ps.println();
        ps.println("package io.github.karlatemp.mxlib.logger;");
        ps.println("public class AsyncLogger implements");
        ps.println(loggerClass.getName());
        ps.println(" {");
        ps.println();
        ps.println("public AsyncLogger(");
        ps.println(loggerClass.getName());
        ps.println("delegate, ");
        ps.println(executorClass.getName());
        ps.println("executor) {this.delegate=delegate;this.executor=executor;}");
        ps.println("private final");
        ps.println(loggerClass.getName());
        ps.println("delegate;private final");
        ps.println(executorClass.getName());
        ps.println("executor;");

        Method[] methods = loggerClass.getDeclaredMethods();
        for (Method m : methods) {
            if (Modifier.isStatic(m.getModifiers())) continue;
            Class<?>[] types = m.getParameterTypes();

            ps.append("public ").append(m.getReturnType().getCanonicalName());
            ps.append(" ").append(m.getName()).append("(");
            String pps;
            {
                Iterator<Class<?>> iterator = Arrays.asList(types).iterator();
                if (iterator.hasNext()) {
                    ps.append(iterator.next().getCanonicalName()).append(" p0");
                }
                int counter = 1;
                while (iterator.hasNext()) {
                    ps.append(", ").append(iterator.next().getCanonicalName());
                    ps.append(" p").print(counter++);
                }
                String[] x = new String[types.length];
                for (int i = 0; i < x.length; i++) {
                    x[i] = "p" + i;
                }
                pps = String.join(",", x);
            }
            ps.println(") {");

            if (m.getReturnType() == void.class) {
                ps.print("executor.execute(()->delegate.");
                ps.print(m.getName());
                ps.println("(");
                ps.println(pps);
                ps.println("));");
            } else {
                ps.println("return delegate." + m.getName() + "(");
                ps.println(pps);
                ps.println(");");
            }
            ps.append("}");
        }

        ps.println();
        ps.println("}");

        if (ps != System.out) {
            ps.close();
        }
    }
}
