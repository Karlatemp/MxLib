package io.github.karlatemp.mxlib.internal;

import io.github.karlatemp.mxlib.utils.Lazy;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Supplier;

public class LazyDelegate<T> implements Lazy<T> {
    private volatile Supplier<T> delegate;
    @SuppressWarnings("rawtypes")
    public static final AtomicReferenceFieldUpdater<LazyDelegate, Supplier> ATOMIC_REFERENCE_FIELD_UPDATER
            = AtomicReferenceFieldUpdater.newUpdater(LazyDelegate.class, Supplier.class, "delegate");

    public LazyDelegate() {
    }

    @Override
    public T get() {
        return delegate.get();
    }

    public Supplier<T> getDelegate() {
        return delegate;
    }

    public void setDelegate(Supplier<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    public LazyDelegate<T> delegated(Supplier<T> delegate) {
        setDelegate(delegate);
        return this;
    }
}
