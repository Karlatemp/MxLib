/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-impl.main/BkSysTranslator.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot.impl;

import io.github.karlatemp.mxlib.common.utils.DelegatedImmutableMap;
import io.github.karlatemp.mxlib.format.FormatTemplate;
import io.github.karlatemp.mxlib.format.Formatter;
import io.github.karlatemp.mxlib.format.common.StringFormatFormatter;
import io.github.karlatemp.mxlib.reflect.Reflections;
import io.github.karlatemp.mxlib.translate.AbstractTranslator;
import io.github.karlatemp.mxlib.translate.SystemTranslator;
import io.github.karlatemp.mxlib.utils.IteratorSupplier;
import io.github.karlatemp.mxlib.utils.NodeConcurrentLinkedList;
import io.github.karlatemp.unsafeaccessor.Root;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static io.github.karlatemp.mxlib.spigot.NmsHelper.getNmsClass;

@SuppressWarnings("unchecked")
public class BkSysTranslator extends AbstractTranslator implements SystemTranslator {
    private static final Class<?> LocaleLanguage = getNmsClass("LocaleLanguage");
    private static final Object localLanguageInstance;
    private static final Map<String, String> OLD_LANGUAGE_REF;
    private static final Field LocalLanguage$map;
    private static final Lock exLock = new ReentrantLock();
    static final Map<String, String> TranslateMapper = new AbstractMap<String, String>() {
        @NotNull
        @Override
        public Set<Entry<String, String>> entrySet() {
            return newMirror().entrySet();
        }

        @Override
        public String get(Object key) {
            for (PrioritizedTranslate pt : IteratorSupplier.by(
                    new NodeConcurrentLinkedList.NodeValueIterator<>(translates.getHead(), false)
            )) {
                Map<String, String> trans = pt.trans;
                String s = trans.get(key);
                if (s != null) return s;
            }
            return null;
        }

        @Override
        public String getOrDefault(Object key, String defaultValue) {
            String s = get(key);
            if (s != null) return s;
            return defaultValue;
        }

        @Override
        public boolean containsKey(Object key) {

            for (PrioritizedTranslate pt : IteratorSupplier.by(
                    new NodeConcurrentLinkedList.NodeValueIterator<>(translates.getHead(), false)
            )) {
                Map<String, String> trans = pt.trans;
                if (trans.containsKey(key)) return true;
            }
            return false;
        }

        private HashMap<String, String> newMirror() {

            HashMap<String, String> he = new HashMap<>();
            for (PrioritizedTranslate pt : IteratorSupplier.by(
                    new NodeConcurrentLinkedList.NodeValueIterator<>(
                            translates.getTail(), true
                    )
            )) {
                he.putAll(pt.trans);
            }
            return he;

        }

        @Override
        public String toString() {
            return newMirror().toString();
        }
    };

    private static final Formatter formatter = new StringFormatFormatter();

    @Override
    protected @Nullable FormatTemplate findTranslateTemplate(@NotNull String key) {
        String template = TranslateMapper.get(key);
        if (template != null) return formatter.parse(template);
        return null;
    }

    static class PrioritizedTranslate {
        int priority;
        Map<String, String> trans;
    }

    private static final NodeConcurrentLinkedList<PrioritizedTranslate> translates = new NodeConcurrentLinkedList<>();

    static void add(PrioritizedTranslate translate) {
        NodeConcurrentLinkedList.Node<PrioritizedTranslate> node = translates.getHead().getNext();
        exLock.lock();
        try {
            while (!node.isTail()) {
                PrioritizedTranslate value = node.getValue();
                if (value.priority > translate.priority) {
                    node.insertBefore(translate);
                    return;
                }
                node = node.getNext();
            }
            node.insertBefore(translate);
        } finally {
            exLock.unlock();
        }
    }

    static void add(int p, Map<String, String> trans) {
        PrioritizedTranslate pt = new PrioritizedTranslate();
        pt.priority = p;
        pt.trans = trans;
        add(pt);
    }

    static {
        try {
            Object localeLanguage;
            try {
                localeLanguage = LocaleLanguage.getMethod("getInstance").invoke(null);
            } catch (Throwable ignored) {
                Field[] fields = LocaleLanguage.getDeclaredFields();
                Field field = null;
                for (Field f : fields) {
                    if (Modifier.isStatic(f.getModifiers())) {
                        if (f.getType() == LocaleLanguage) {
                            field = f;
                            break;
                        }
                    }
                }
                if (field == null) {
                    throw new NoSuchFieldException("static " + LocaleLanguage.getName() + " *: " + LocaleLanguage.getName());
                }
                Root.openAccess(field);
                localeLanguage = field.get(null);
            }
            localLanguageInstance = localeLanguage;
            Class<?> klass = localeLanguage.getClass();
            //noinspection OptionalGetWithoutIsPresent
            LocalLanguage$map = Reflections.allFields(klass)
                    .filter(Reflections.ModifierFilter.NON_STATIC)
                    .filter(it -> Map.class.isAssignableFrom(it.getType()))
                    .findFirst()
                    .get();
            OLD_LANGUAGE_REF = (Map<String, String>) Root.openAccess(LocalLanguage$map).get(localeLanguage);
            LocalLanguage$map.set(localeLanguage, DelegatedImmutableMap.newDelegatedImmutableMap(TranslateMapper));
        } catch (Throwable exception) {
            throw new ExceptionInInitializerError(exception);
        }
        add(Integer.MAX_VALUE, OLD_LANGUAGE_REF);
    }
}
