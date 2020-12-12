/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTTagLongArray.java
 */

package io.github.karlatemp.mxlib.nbt;

import io.github.karlatemp.mxlib.nbt.visitor.NBTVisitor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagLongArray extends NBTList<NBTTagLong> {
    private long[] data;

    public NBTTagLongArray(long[] array) {
        this.data = array;
    }

    public NBTTagLongArray(List<Long> array) {
        this(a(array));
    }

    public NBTTagLongArray() {
    }

    private static long[] a(List<Long> list) {
        long[] abyte = new long[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            Long obyte = list.get(i);

            abyte[i] = obyte == null ? 0 : obyte;
        }

        return abyte;
    }

    @Override
    public NBTTagLong get(int index) {
        return new NBTTagLong(data[index]);
    }

    @Override
    public NBTTagLong set(int index, NBTTagLong value) {
        long o = data[index];
        data[index] = value.asByte();
        return new NBTTagLong(o);
    }

    @Override
    public void add(int index, NBTTagLong value) {
        add(index, value.asByte());
    }

    private void add(int index, long v) {
        long[] sw = new long[data.length + 1];
        sw[index] = v;
        System.arraycopy(data, 0, sw, 0, index);
        System.arraycopy(data, index, sw, index + 1, data.length - index);
        data = sw;
    }

    @Override
    public NBTTagLong remove(int index) {
        long v = data[index];
        long[] aw = new long[data.length - 1];
        System.arraycopy(data, 0, aw, 0, index);
        System.arraycopy(data, index + 1, aw, index, aw.length - index);
        return new NBTTagLong(v);
    }

    @Override
    public boolean setValue(int i, NBTBase base) {
        if (base instanceof NBTNumber) {
            data[i] = ((NBTNumber) base).asLong();
            return true;
        }
        return false;
    }

    @Override
    public boolean addValue(int i, NBTBase base) {
        if (base instanceof NBTNumber) {
            add(i, ((NBTNumber) base).asLong());
            return true;
        }
        return false;
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitLongArray(name, this);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(data.length);
        for (long i : data) {
            out.writeLong(i);
        }
    }

    @Override
    public void load(DataInput input, int i, NBTReadLimiter limiter) throws IOException {
        limiter.read(192L);
        int size = input.readInt();
        limiter.read(64 * size);
        data = new long[size];
        for (int w = 0; w < size; w++) {
            data[w] = input.readLong();
        }
    }

    @Override
    public byte getTypeId() {
        return 12;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[L;");
        for (int i = 0; i < data.length; i++) {
            if (i != 0) {
                builder.append(',');
            }
            builder.append(data[i]).append('L');
        }
        return builder.append(']').toString();
    }

    @Override
    public NBTTagLongArray clone() {
        long[] arr = new long[data.length];
        System.arraycopy(data, 0, arr, 0, arr.length);
        return new NBTTagLongArray(arr);
    }

    @Override
    public int size() {
        return data.length;
    }

    public void clear() {
        this.data = new long[0];
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagLongArray && Arrays.equals(this.data, ((NBTTagLongArray) object).data);
    }

    public long[] getLongs() {
        return data;
    }
}
