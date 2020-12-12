/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTTagByteArray.java
 */

package io.github.karlatemp.mxlib.nbt;

import io.github.karlatemp.mxlib.nbt.visitor.NBTVisitor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class NBTTagByteArray extends NBTList<NBTTagByte> {
    private byte[] data;

    public NBTTagByteArray(byte[] array) {
        this.data = array;
    }

    public NBTTagByteArray(List<Byte> array) {
        this(a(array));
    }

    public NBTTagByteArray() {
    }

    private static byte[] a(List<Byte> list) {
        byte[] abyte = new byte[list.size()];

        for (int i = 0; i < list.size(); ++i) {
            Byte obyte = list.get(i);

            abyte[i] = obyte == null ? 0 : obyte;
        }

        return abyte;
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitByteArray(name, this);
    }

    @Override
    public NBTTagByte get(int index) {
        return new NBTTagByte(data[index]);
    }

    @Override
    public NBTTagByte set(int index, NBTTagByte value) {
        byte o = data[index];
        data[index] = value.asByte();
        return new NBTTagByte(o);
    }

    @Override
    public void add(int index, NBTTagByte value) {
        add(index, value.asByte());
    }

    private void add(int index, byte v) {
        byte[] sw = new byte[data.length + 1];
        sw[index] = v;
        System.arraycopy(data, 0, sw, 0, index);
        System.arraycopy(data, index, sw, index + 1, data.length - index);
        data = sw;
    }

    @Override
    public NBTTagByte remove(int index) {
        byte v = data[index];
        byte[] aw = new byte[data.length - 1];
        System.arraycopy(data, 0, aw, 0, index);
        System.arraycopy(data, index + 1, aw, index, aw.length - index);
        return new NBTTagByte(v);
    }

    @Override
    public boolean setValue(int i, NBTBase base) {
        if (base instanceof NBTNumber) {
            data[i] = ((NBTNumber) base).asByte();
            return true;
        }
        return false;
    }

    @Override
    public boolean addValue(int i, NBTBase base) {
        if (base instanceof NBTNumber) {
            add(i, ((NBTNumber) base).asByte());
            return true;
        }
        return false;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(data.length);
        out.write(data);
    }

    @Override
    public void load(DataInput input, int i, NBTReadLimiter limiter) throws IOException {
        limiter.read(192L);
        int ln = input.readInt();
        limiter.read(8 * ln);
        data = new byte[ln];
        input.readFully(data);
    }

    @Override
    public byte getTypeId() {
        return 7;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[B;");
        for (int i = 0; i < data.length; i++) {
            if (i != 0) {
                builder.append(',');
            }
            builder.append(data[i]).append('B');
        }
        return builder.append(']').toString();
    }

    @Override
    public NBTTagByteArray clone() {
        byte[] arr = new byte[data.length];
        System.arraycopy(data, 0, arr, 0, arr.length);
        return new NBTTagByteArray(arr);
    }

    @Override
    public int size() {
        return data.length;
    }

    public void clear() {
        this.data = new byte[0];
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagByteArray && Arrays.equals(this.data, ((NBTTagByteArray) object).data);
    }

    public byte[] getBytes() {
        return data;
    }
}
