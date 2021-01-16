/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/NodeConcurrentLinkedList.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NodeConcurrentLinkedList<T> {
    public static abstract class Node<T> {
        protected Lock lock;
        protected Node<T> prev, next;
        protected boolean inserted = false;

        public abstract T getValue();

        public abstract void setValue(T value);

        public abstract boolean isHead();

        public abstract boolean isTail();

        public void insertBefore(@NotNull Node<T> prev) {
            lock.lock();
            try {
                if (prev.inserted) throw new IllegalArgumentException("`prev` was inserted");
                Node<T> p = this.prev;
                p.next = prev;
                prev.prev = p;
                prev.next = this;
                prev.inserted = true;
                this.prev = prev;
            } finally {
                lock.unlock();
            }
        }

        public void insertAfter(@NotNull Node<T> next) {
            lock.lock();
            try {
                if (next.inserted) throw new IllegalArgumentException("`next` was inserted");
                Node<T> n = this.next;
                n.prev = next;
                next.next = n;
                next.prev = this;
                next.inserted = true;
                this.next = next;
            } finally {
                lock.unlock();
            }
        }

        protected @NotNull Value<T> newNode(T value) {
            Value<T> val = new Value<>();
            val.value = value;
            val.lock = this.lock;
            return val;
        }

        public @NotNull Node<T> insertBefore(T value) {
            Node<T> n = newNode(value);
            insertBefore(n);
            return n;
        }

        public @NotNull Node<T> insertAfter(T value) {
            Node<T> n = newNode(value);
            insertAfter(n);
            return n;
        }

        public Node<T> getPrev() {
            return prev;
        }

        public Node<T> getNext() {
            return next;
        }

        public void remove() {
            lock.lock();
            try {
                if (!inserted) return;
                Node<T> prev = this.prev, next = this.next;
                prev.next = next;
                next.prev = prev;
                inserted = false;
            } finally {
                lock.unlock();
            }
        }
    }

    public static class Head<T> extends Node<T> {
        @Override
        public T getValue() {
            return null;
        }

        @Override
        public void setValue(T value) {
        }

        @Override
        public boolean isHead() {
            return true;
        }

        @Override
        public boolean isTail() {
            return false;
        }

        @Override
        @Deprecated
        public void insertBefore(@NotNull Node<T> prev) {
            insertAfter(prev);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static class Tail<T> extends Node<T> {

        @Override
        public T getValue() {
            return null;
        }

        @Override
        public void setValue(T value) {
        }

        @Override
        public boolean isHead() {
            return false;
        }

        @Override
        public boolean isTail() {
            return true;
        }

        @Override
        @Deprecated
        public void insertAfter(@NotNull Node<T> next) {
            insertBefore(next);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static class Value<T> extends Node<T> {
        protected T value;

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public void setValue(T value) {
            this.value = value;
        }

        @Override
        public boolean isHead() {
            return false;
        }

        @Override
        public boolean isTail() {
            return false;
        }
    }

    protected final Head<T> head = new Head<>();
    protected final Tail<T> tail = new Tail<>();
    protected final Lock lock;

    public NodeConcurrentLinkedList() {
        this(new ReentrantLock());
    }

    public NodeConcurrentLinkedList(Lock lock) {
        this.lock = lock;
        head.lock = tail.lock = this.lock;
    }

    {
        head.next = tail;
        tail.prev = head;
    }

    public Head<T> getHead() {
        return head;
    }

    public Tail<T> getTail() {
        return tail;
    }

    static class NodeIterator<T> {
        private Node<T> start;
        private final boolean rev;
        private Node<T> cachedNext = null;
        private boolean nexted = false;
        private Node<T> current;

        NodeIterator(Node<T> start, boolean rev) {
            this.start = start;
            this.rev = rev;
            if (!start.isHead() && !start.isTail()) {
                cachedNext = start;
                nexted = true;
            }
        }

        public boolean hasNext() {
            if (nexted) {
                return cachedNext != null;
            }
            nexted = true;
            Node<T> n = rev ? start.prev : start.next;
            if (n.isTail() || n.isHead()) {
                cachedNext = null;
            } else {
                cachedNext = n;
            }
            return cachedNext != null;
        }

        Node<T> nextNode() {
            current = null;
            if (hasNext()) {
                Node<T> n = cachedNext;
                cachedNext = null;
                start = n;
                nexted = false;
                current = n;
                return n;
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            current.remove();
        }
    }

    public static class NodeValueIterator<T>
            extends NodeIterator<T>
            implements Iterator<T> {

        public NodeValueIterator(Node<T> start, boolean rev) {
            super(start, rev);
        }

        @Override
        public T next() {
            return nextNode().getValue();
        }
    }

    public static class NodeNodeIterator<T>
            extends NodeIterator<T>
            implements Iterator<Node<T>> {

        public NodeNodeIterator(Node<T> start, boolean rev) {
            super(start, rev);
        }

        @Override
        public Node<T> next() {
            return nextNode();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        NodeValueIterator<T> iterator = new NodeValueIterator<>(head, false);
        if (iterator.hasNext()) {
            builder.append(iterator.next());
        }
        while (iterator.hasNext()) {
            builder.append(", ").append(iterator.next());
        }
        return builder.append(']').toString();
    }
}
