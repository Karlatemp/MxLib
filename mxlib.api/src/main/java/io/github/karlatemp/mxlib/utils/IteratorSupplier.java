/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/IteratorSupplier.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class IteratorSupplier<T> implements Iterable<T>, Iterator<T>, Supplier<T> {

    @Contract(pure = true)
    public static @NotNull <T> IteratorSupplier<T> by(@NotNull Iterator<T> iterator) {
        return new IteratorSupplier<>(iterator);
    }

    private final Iterator<T> iterator;

    @Contract(pure = true)
    public IteratorSupplier(@NotNull Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return iterator;
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        iterator.forEachRemaining(action);
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }

    @Override
    public void remove() {
        iterator.remove();
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        iterator.forEachRemaining(action);
    }


    @Override
    public T get() {
        if (hasNext())
            return next();
        return noNextValue();
    }

    protected T noNextValue() {
        return null;
    }
}
