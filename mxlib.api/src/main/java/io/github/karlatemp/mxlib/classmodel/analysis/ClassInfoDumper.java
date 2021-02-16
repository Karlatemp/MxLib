/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/ClassInfoDumper.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.analysis;

import io.github.karlatemp.mxlib.classmodel.ClassInfo;
import io.github.karlatemp.mxlib.classmodel.ConstructorInfo;
import io.github.karlatemp.mxlib.classmodel.FieldInfo;
import io.github.karlatemp.mxlib.classmodel.MethodInfo;
import io.github.karlatemp.mxlib.utils.LineWriter;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.karlatemp.mxlib.utils.StringBuilderFormattable.by;

public class ClassInfoDumper {
    public static void dump(ClassInfo classInfo, LineWriter writer) {
        writer.println(classInfo);
        {
            ClassInfo x = classInfo.getSuperclass();
            while (x != null) {
                writer.println(by("  `- ").plusMsg(x));
                x = x.getSuperclass();
            }
        }
        writer.println("Interfaces:");
        for (ClassInfo i : classInfo.getInterfaces()) {
            writer.println(by(" - ").plusMsg(i));
        }
        writer.println("=============");
        writer.println("Constructors:");
        for (ConstructorInfo c : classInfo.getConstructors()) {
            writer.println(by("- ").plusMsg(c));
        }

        writer.println("Fields:");
        {
            Map<ClassInfo, List<FieldInfo>> fields = new HashMap<>();
            for (FieldInfo fieldInfo : classInfo.getFields()) {
                fields.computeIfAbsent(fieldInfo.getDeclaredClass(), $ -> new ArrayList<>()).add(fieldInfo);
            }
            for (Map.Entry<ClassInfo, List<FieldInfo>> a : fields.entrySet()) {
                writer.println(by("- ").plusMsg(a.getKey()));
                for (FieldInfo fieldInfo : a.getValue()) {
                    writer.println(by("|   `- ").plusMsg(fieldInfo));
                }
            }
        }

        writer.println("Methods:");
        {
            Map<ClassInfo, List<MethodInfo>> methods = new HashMap<>();
            List<MethodInfo> allMethods = classInfo.getMethods();
            for (MethodInfo methodInfo : allMethods) {
                methods.computeIfAbsent(methodInfo.getDeclaredClass(), $ -> new ArrayList<>()).add(methodInfo);
            }

            for (Map.Entry<ClassInfo, List<MethodInfo>> a : methods.entrySet()) {
                writer.println(by("- ").plusMsg(a.getKey()));
                for (MethodInfo methodInfo : a.getValue()) {
                    writer.println(by("|   `- ").plusMsg(methodInfo));

                    if (!Modifier.isStatic(methodInfo.getModifiers())) {
                        for (MethodInfo mi : allMethods) {
                            if (Modifier.isStatic(mi.getModifiers())) continue;
                            if (mi == methodInfo) continue;

                            if (!mi.getName().equals(methodInfo.getName())) continue;
                            if (!mi.getReturnType().equals(methodInfo.getReturnType())) continue;
                            if (!mi.getArgumentTypes().equals(methodInfo.getArgumentTypes())) continue;

                            writer.println(by("|      + ").plusMsg(mi.getDeclaredClass()));
                        }
                    }
                    List<MethodInfo> overrides = methodInfo.getOverrides();

                    if (!overrides.isEmpty()) {
                        for (MethodInfo override : overrides) {
                            writer.println(by("|      <- ").plusMsg(override.getDeclaredClass().getName()));
                        }
                    }
                }
            }
        }
    }
}
