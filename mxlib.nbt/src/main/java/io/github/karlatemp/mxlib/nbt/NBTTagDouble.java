/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-nbt.main/NBTTagDouble.java
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

public class NBTTagDouble implements NBTNumber {
    private double data;

    public NBTTagDouble(double data) {
        this.data = data;
    }

    public NBTTagDouble() {
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
        return (float) data;
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitDouble(name, this);
    }

    @Override
    public Number getValue() {
        return data;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeDouble(data);
    }

    @Override
    public void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException {
        limiter.read(128L);
        data = input.readDouble();
    }

    @Override
    public String toString() {
        return data + "d";
    }

    @Override
    public byte getTypeId() {
        return 6;
    }

    @Override
    public int hashCode() {
        long i = Double.doubleToLongBits(this.data);
        return (int) (i ^ i >>> 32);
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagDouble && this.data == ((NBTTagDouble) object).data;
    }

    @Override
    public NBTTagDouble clone() {
        return new NBTTagDouble(data);
    }
}
