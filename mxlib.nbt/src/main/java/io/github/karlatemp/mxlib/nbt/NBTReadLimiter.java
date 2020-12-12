/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTReadLimiter.java
 */

package io.github.karlatemp.mxlib.nbt;

public class NBTReadLimiter {
    private final long max;
    private long reade;
    public static final NBTReadLimiter UN_LIMITED = new NBTReadLimiter(0) {
        @Override
        public void read(long bits) {
        }
    };

    public NBTReadLimiter(long size) {
        this.max = size;
    }

    public void read(long bits) {
        reade += bits / Byte.SIZE;
        if (reade > max) {
            throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.reade + "bytes where max allowed: " + this.max);
        }
    }
}
