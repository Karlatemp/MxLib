/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTTagByte.java
 */

package io.github.karlatemp.mxlib.nbt;

import io.github.karlatemp.mxlib.nbt.visitor.NBTVisitor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagByte implements NBTNumber {
    private byte data;

    public NBTTagByte(byte data) {
        this.data = data;
    }

    public NBTTagByte() {
    }

    @Override
    public long asLong() {
        return data;
    }

    @Override
    public int asInt() {
        return data;
    }

    @Override
    public short asShort() {
        return data;
    }

    @Override
    public byte asByte() {
        return data;
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
        out.writeByte(data);
    }

    @Override
    public void load(DataInput input, int i, NBTReadLimiter limiter) throws IOException {
        limiter.read(72L);
        data = input.readByte();
    }

    @Override
    public byte getTypeId() {
        return 1;
    }

    @Override
    public NBTTagByte clone() {
        return new NBTTagByte(data);
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitByte(name, this);
    }

    @Override
    public String toString() {
        return data + "b";
    }

    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagByte && this.data == ((NBTTagByte) object).data;
    }

    public int hashCode() {
        return this.data;
    }

}
