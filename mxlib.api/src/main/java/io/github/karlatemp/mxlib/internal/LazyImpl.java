package io.github.karlatemp.mxlib.internal;

import io.github.karlatemp.mxlib.utils.Lazy;
import io.github.karlatemp.mxlib.utils.Suppliers;

import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

public class LazyImpl {
    public abstract static class DelegatedLazy<T> implements Lazy<T> {

        protected final LazyDelegate<T> delegate;
        protected Supplier<T> supplier;

        public DelegatedLazy(LazyDelegate<T> delegated, Supplier<T> supplier) {
            this.supplier = supplier;
            this.delegate = delegated;
        }
    }

    public static class SynchronizedLazy<T> extends DelegatedLazy<T> {
        public SynchronizedLazy(LazyDelegate<T> delegated, Supplier<T> supplier) {
            super(delegated, supplier);
        }

        @Override
        public synchronized T get() {
            if (delegate.getDelegate() != this) return delegate.get();
            T t = supplier.get();
            supplier = null;
            delegate.setDelegate(Suppliers.constant(t));
            return t;
        }
    }

    public static class LockedLazy<T> extends DelegatedLazy<T> {

        private final Lock lock;

        public LockedLazy(LazyDelegate<T> delegated, Supplier<T> supplier, Lock lock) {
            super(delegated, supplier);
            this.lock = lock;
        }

        @Override
        public T get() {
            lock.lock();
            try {
                if (delegate.get() != this) return delegate.get();
                T v = supplier.get();
                delegate.setDelegate(Suppliers.constant(v));
                return v;
            } finally {
                lock.unlock();
            }
        }
    }

    public static class PublicationLazy<T> extends DelegatedLazy<T> {
        public PublicationLazy(LazyDelegate<T> delegated, Supplier<T> supplier) {
            super(delegated, supplier);
        }

        @Override
        public T get() {
            T t = supplier.get();
            LazyDelegate.ATOMIC_REFERENCE_FIELD_UPDATER.compareAndSet(
                    delegate, this, Suppliers.constant(t)
            );
            return t;
        }
    }

    public static class NonThreadSafeLazy<T> extends DelegatedLazy<T> {

        public NonThreadSafeLazy(LazyDelegate<T> delegated, Supplier<T> supplier) {
            super(delegated, supplier);
        }

        @Override
        public T get() {
            T t = supplier.get();
            delegate.setDelegate(Suppliers.constant(t));
            return t;
        }
    }
}
