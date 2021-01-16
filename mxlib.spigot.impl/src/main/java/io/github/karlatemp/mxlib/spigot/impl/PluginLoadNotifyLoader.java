/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-spigot-impl.main/PluginLoadNotifyLoader.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.spigot.impl;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;


@SuppressWarnings({"rawtypes", "unchecked", "NullableProblems"})
public class PluginLoadNotifyLoader extends AbstractList {
    private final List delegate;

    public PluginLoadNotifyLoader(List delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean add(Object o) {
        for (Consumer<Object> c : Notifications.PluginLoadEvent) {
            c.accept(o);
        }
        return delegate.add(o);
    }

    @Override
    public boolean remove(Object o) {
        for (Consumer<Object> c : Notifications.PluginUnLoadEvent) {
            c.accept(o);
        }
        return delegate.remove(o);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    @Override
    public Iterator iterator() {
        return delegate.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return delegate.toArray();
    }

    @NotNull
    @Override
    public Object[] toArray(@NotNull Object[] a) {
        return delegate.toArray(a);
    }


    @Override
    public boolean containsAll(@NotNull Collection c) {
        return delegate.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection c) {
        return delegate.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection c) {
        return delegate.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection c) {
        return delegate.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection c) {
        return delegate.retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator operator) {
        delegate.replaceAll(operator);
    }

    @Override
    public void sort(Comparator c) {
        delegate.sort(c);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public Object get(int index) {
        return delegate.get(index);
    }

    @Override
    public Object set(int index, Object element) {
        return delegate.set(index, element);
    }

    @Override
    public void add(int index, Object element) {
        delegate.add(index, element);
    }

    @Override
    public Object remove(int index) {
        return delegate.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return delegate.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return delegate.lastIndexOf(o);
    }

    @Override
    public ListIterator listIterator() {
        return delegate.listIterator();
    }

    @Override
    public ListIterator listIterator(int index) {
        return delegate.listIterator(index);
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return delegate.subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator spliterator() {
        return delegate.spliterator();
    }
}
