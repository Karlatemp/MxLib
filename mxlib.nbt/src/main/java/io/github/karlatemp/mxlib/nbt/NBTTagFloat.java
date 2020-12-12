/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTTagFloat.java
 */

package io.github.karlatemp.mxlib.nbt;

import io.github.karlatemp.mxlib.nbt.visitor.NBTVisitor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagFloat implements NBTNumber {
    private float data;

    public NBTTagFloat(float data) {
        this.data = data;
    }

    public NBTTagFloat() {
    }

    @Override
    public long asLong() {
        return (long) data;
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
        out.writeFloat(data);
    }

    @Override
    public void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException {
        limiter.read(96L);
        data = input.readFloat();
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitFloat(name, this);
    }

    @Override
    public String toString() {
        return data + "f";
    }

    @Override
    public byte getTypeId() {
        return 5;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(data);
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagFloat && this.data == ((NBTTagFloat) object).data;
    }

    @Override
    public NBTTagFloat clone() {
        return new NBTTagFloat(data);
    }
}
