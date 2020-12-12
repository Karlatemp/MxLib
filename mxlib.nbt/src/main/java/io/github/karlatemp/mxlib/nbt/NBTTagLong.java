/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTTagLong.java
 */

package io.github.karlatemp.mxlib.nbt;

import io.github.karlatemp.mxlib.nbt.visitor.NBTVisitor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagLong implements NBTNumber {
    private long data;

    public NBTTagLong(long data) {
        this.data = data;
    }

    public NBTTagLong() {
    }

    @Override
    public long asLong() {
        return data;
    }

    @Override
    public int asInt() {
        return (int) data;
    }

    @Override
    public short asShort() {
        return (short) data;
    }

    @Override
    public byte asByte() {
        return (byte) data;
    }

    @Override
    public double asDouble() {
        return data;
    }

    @Override
    public float asFloat() {
        return data;
    }

    @Override
    public Number getValue() {
        return data;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(data);
    }

    @Override
    public void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException {
        limiter.read(128L);
        data = input.readLong();
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitLong(name, this);
    }

    @Override
    public String toString() {
        return data + "L";
    }

    @Override
    public byte getTypeId() {
        return 4;
    }

    @Override
    public int hashCode() {
        return (int) (this.data ^ this.data >>> 32);
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagLong && this.data == ((NBTTagLong) object).data;
    }

    @Override
    public NBTTagLong clone() {
        return new NBTTagLong(data);
    }
}
