/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/Lazy.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import io.github.karlatemp.mxlib.internal.LazyDelegate;
import io.github.karlatemp.mxlib.internal.LazyImpl;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

public interface Lazy<T> extends Supplier<T> {
    T get();

    static <T> Lazy<T> lazy(@NotNull Supplier<T> supplier) {
        LazyDelegate<T> delegated = new LazyDelegate<>();
        return delegated.delegated(new LazyImpl.SynchronizedLazy<>(delegated, supplier));
    }

    static <T> Lazy<T> publication(@NotNull Supplier<T> supplier) {
        LazyDelegate<T> delegated = new LazyDelegate<>();
        return delegated.delegated(new LazyImpl.PublicationLazy<>(delegated, supplier));
    }

    static <T> Lazy<T> constant(@NotNull T value) {
        return (Lazy<T>) Suppliers.constant(value);
    }

    static <T> Lazy<T> locked(@NotNull Supplier<T> supplier, @NotNull Lock lock) {
        LazyDelegate<T> delegated = new LazyDelegate<>();
        return delegated.delegated(new LazyImpl.LockedLazy<>(delegated, supplier, lock));
    }
}
