/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-nbt.main/NBTTagShort.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.nbt;

import io.github.karlatemp.mxlib.nbt.visitor.NBTVisitor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort implements NBTNumber {
    private short data;

    public NBTTagShort(short data) {
        this.data = data;
    }

    public NBTTagShort() {
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
        return data;
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
        out.writeShort(data);
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitShort(name, this);
    }

    @Override
    public void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException {
        limiter.read(80L);
        data = input.readShort();
    }

    @Override
    public String toString() {
        return data + "s";
    }

    @Override
    public byte getTypeId() {
        return 2;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(data);
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagShort && this.data == ((NBTTagShort) object).data;
    }

    @Override
    public NBTTagShort clone() {
        return new NBTTagShort(data);
    }
}
