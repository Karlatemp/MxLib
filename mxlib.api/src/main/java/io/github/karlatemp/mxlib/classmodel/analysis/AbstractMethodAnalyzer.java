/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/AbstractMethodAnalyzer.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.classmodel.analysis;

import io.github.karlatemp.mxlib.classmodel.MethodInfo;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AbstractMethodAnalyzer {
    public static List<MethodInfo> findAbstractMethods(List<MethodInfo> methods) {
        List<MethodInfo> response = new ArrayList<>();
        for (MethodInfo methodInfo : methods) {
            if (Modifier.isAbstract(methodInfo.getModifiers())) {
                response.add(methodInfo);
            }
        }
        Iterator<MethodInfo> iterator = response.iterator();
        while (iterator.hasNext()) {
            MethodInfo next = iterator.next();
            for (MethodInfo methodInfo : methods) {
                if (Modifier.isStatic(methodInfo.getModifiers())) continue;
                if (Modifier.isAbstract(methodInfo.getModifiers())) continue;
                if (!next.getName().equals(methodInfo.getName())) continue;
                if (!next.getReturnType().equals(methodInfo.getReturnType())) continue;
                if (!next.getArgumentTypes().equals(methodInfo.getArgumentTypes())) continue;
                iterator.remove();
                break;
            }
        }
        return response;
    }

    public static MethodInfo findSingleAbstractMethod(List<MethodInfo> abstractMethods) throws NoSuchMethodException {
        List<MethodInfo> methods = new ArrayList<>();
        top:
        for (MethodInfo m : abstractMethods) {
            if (!Modifier.isAbstract(m.getModifiers())) continue;

            for (MethodInfo exists : methods) {
                if (!exists.getName().equals(m.getName())) continue;
                if (!exists.getReturnType().equals(m.getReturnType())) continue;
                if (!exists.getArgumentTypes().equals(m.getArgumentTypes())) continue;
                continue top;
            }
            methods.add(m);
        }
        if (methods.isEmpty()) {
            throw new NoSuchMethodException("No abstract method found.");
        }
        if (methods.size() == 1) return methods.get(0);

        throw new NoSuchMethodException("Multiple abstract methods: " + methods);
    }
}
