/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-nbt.main/NBTTagIntArray.java
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
import java.util.Arrays;
import java.util.List;

public class NBTTagIntArray extends NBTList<NBTTagInt> {
    private int[] data;

    public NBTTagIntArray(int[] array) {
        this.data = array;
    }

    public NBTTagIntArray(List<Integer> array) {
        this(a(array));
    }

    public NBTTagIntArray() {
    }

    private static int[] a(List<Integer> list) {
        int[] abyte = new int[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            Integer obyte = list.get(i);

            abyte[i] = obyte == null ? 0 : obyte;
        }

        return abyte;
    }

    @Override
    public NBTTagInt get(int index) {
        return new NBTTagInt(data[index]);
    }

    @Override
    public NBTTagInt set(int index, NBTTagInt value) {
        int o = data[index];
        data[index] = value.asByte();
        return new NBTTagInt(o);
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitIntArray(name, this);
    }

    @Override
    public void add(int index, NBTTagInt value) {
        add(index, value.asByte());
    }

    private void add(int index, int v) {
        int[] sw = new int[data.length + 1];
        sw[index] = v;
        System.arraycopy(data, 0, sw, 0, index);
        System.arraycopy(data, index, sw, index + 1, data.length - index);
        data = sw;
    }

    @Override
    public NBTTagInt remove(int index) {
        int v = data[index];
        int[] aw = new int[data.length - 1];
        System.arraycopy(data, 0, aw, 0, index);
        System.arraycopy(data, index + 1, aw, index, aw.length - index);
        return new NBTTagInt(v);
    }

    @Override
    public boolean setValue(int i, NBTBase base) {
        if (base instanceof NBTNumber) {
            data[i] = ((NBTNumber) base).asInt();
            return true;
        }
        return false;
    }

    @Override
    public boolean addValue(int i, NBTBase base) {
        if (base instanceof NBTNumber) {
            add(i, ((NBTNumber) base).asInt());
            return true;
        }
        return false;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(data.length);
        for (int i : data) {
            out.writeInt(i);
        }
    }

    @Override
    public void load(DataInput input, int i, NBTReadLimiter limiter) throws IOException {
        limiter.read(192L);
        int size = input.readInt();
        limiter.read(32 * size);
        data = new int[size];
        for (int w = 0; w < size; w++) {
            data[w] = input.readInt();
        }
    }

    @Override
    public byte getTypeId() {
        return 11;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[I;");
        for (int i = 0; i < data.length; i++) {
            if (i != 0) {
                builder.append(',');
            }
            builder.append(data[i]);
        }
        return builder.append(']').toString();
    }

    @Override
    public NBTTagIntArray clone() {
        int[] arr = new int[data.length];
        System.arraycopy(data, 0, arr, 0, arr.length);
        return new NBTTagIntArray(arr);
    }

    @Override
    public int size() {
        return data.length;
    }

    public void clear() {
        this.data = new int[0];
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagIntArray && Arrays.equals(this.data, ((NBTTagIntArray) object).data);
    }

    public int[] getInts() {
        return data;
    }
}
