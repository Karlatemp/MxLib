/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/NoLock.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public enum NoLock implements Lock, Condition {
    INSTANCE;

    @Override
    public void lock() {
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
    }

    @Override
    public boolean tryLock() {
        return true;
    }

    @Override
    public boolean tryLock(long time, @NotNull TimeUnit unit) throws InterruptedException {
        return true;
    }

    @Override
    public void unlock() {

    }

    @NotNull
    @Override
    public Condition newCondition() {
        return this;
    }

    @Override
    public void await() throws InterruptedException {
    }

    @Override
    public void awaitUninterruptibly() {
    }

    @Override
    public long awaitNanos(long nanosTimeout) throws InterruptedException {
        return nanosTimeout;
    }

    @Override
    public boolean await(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public boolean awaitUntil(@NotNull Date deadline) throws InterruptedException {
        return false;
    }

    @Override
    public void signal() {
    }

    @Override
    public void signalAll() {
    }
}
