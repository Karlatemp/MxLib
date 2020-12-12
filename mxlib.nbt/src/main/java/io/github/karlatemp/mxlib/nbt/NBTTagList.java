/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTTagList.java
 */

package io.github.karlatemp.mxlib.nbt;

import io.github.karlatemp.mxlib.nbt.visitor.NBTVisitor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NBTTagList extends NBTList<NBTBase> {
    private List<NBTBase> list = new ArrayList<>();
    private byte type = 0;

    public NBTTagList() {
    }

    @Override
    public void write(DataOutput out) throws IOException {
        if (list.isEmpty()) type = 0;
        else type = list.get(0).getTypeId();
        out.writeByte(type);
        out.writeInt(list.size());
        for (NBTBase b : list) {
            b.write(out);
        }
    }

    @Override
    public void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException {
        limiter.read(296);
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        } else {
            this.type = input.readByte();
            int size = input.readInt();
            if (type == 0 && size > 0) {
                throw new RuntimeException("Missing type on ListTag");
            } else {
                limiter.read(32L * size);
                while (size-- > 0) {
                    NBTBase base = NBTBase.createTag(type);
                    base.load(input, depth + 1, limiter);
                    list.add(base);
                }
            }
        }
    }

    @Override
    public byte getTypeId() {
        return 9;
    }

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("[");

        for (int i = 0; i < this.list.size(); ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append(this.list.get(i));
        }

        return stringbuilder.append(']').toString();
    }

    private void f() {
        if (list.isEmpty()) type = 0;
    }

    @Override
    public NBTBase remove(int index) {
        NBTBase base = list.remove(index);
        f();
        return base;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public int size() {
        return list.size();
    }

    public NBTTagList getList(int index) {
        if (index >= 0 && index < list.size()) {
            NBTBase base = list.get(index);
            if (base.getTypeId() == 9)
                return (NBTTagList) base;
        }
        return new NBTTagList();
    }

    public short getShort(int index) {
        if (index >= 0 && index < list.size()) {
            NBTBase base = list.get(index);
            if (base.getTypeId() == 2)
                return ((NBTTagShort) base).asShort();
        }
        return 0;
    }

    public int getInt(int index) {
        if (index >= 0 && index < list.size()) {
            NBTBase base = list.get(index);
            if (base.getTypeId() == 3)
                return ((NBTTagInt) base).asInt();
        }
        return 0;
    }

    public int[] getInts(int index) {
        if (index >= 0 && index < list.size()) {
            NBTBase base = list.get(index);
            if (base.getTypeId() == 11)
                return ((NBTTagIntArray) base).getInts();
        }
        return new int[0];
    }

    public double getDouble(int index) {
        if (index >= 0 && index < list.size()) {
            NBTBase base = list.get(index);
            if (base.getTypeId() == 6)
                return ((NBTTagDouble) base).asDouble();
        }
        return 0;
    }

    public float getFloat(int index) {
        if (index >= 0 && index < list.size()) {
            NBTBase base = list.get(index);
            if (base.getTypeId() == 5)
                return ((NBTTagFloat) base).asFloat();
        }
        return 0;
    }

    public String getString(int index) {
        if (index >= 0 && index < list.size()) {
            NBTBase base = list.get(index);
            if (base.getTypeId() == 8)
                return base.asString();
            return base.toString();
        }
        return "";
    }

    private boolean check(NBTBase base) {
        if (base.getTypeId() == 0) return false;
        if (type == 0) {
            type = base.getTypeId();
            return true;
        }
        return type == base.getTypeId();
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitList(name, this);
    }

    @Override
    public NBTBase get(int index) {
        return list.get(index);
    }

    @Override
    public NBTBase set(int index, NBTBase value) {
        if (!check(value)) {
            throw new UnsupportedOperationException(String.format("Trying to add tag of type %d to list of %d", value.getTypeId(), this.type));
        }
        return list.set(index, value);
    }

    @Override
    public void add(int index, NBTBase value) {
        if (!check(value)) {
            throw new UnsupportedOperationException(String.format("Trying to add tag of type %d to list of %d", value.getTypeId(), this.type));
        }
        list.add(index, value);
    }

    @Override
    public boolean setValue(int i, NBTBase base) {
        if (check(base)) {
            list.set(i, base);
            return true;
        }
        return false;
    }

    @Override
    public boolean addValue(int i, NBTBase base) {
        if (check(base)) {
            list.add(i, base);
            return true;
        }
        return false;
    }

    @Override
    public NBTTagList clone() {
        NBTTagList tl = new NBTTagList();
        tl.list.addAll(list);
        tl.type = type;
        return tl;
    }

    @Override
    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagList && Objects.equals(this.list, ((NBTTagList) object).list);
    }

    public int hashCode() {
        return this.list.hashCode();
    }

    public byte getType() {
        return type;
    }

    @Override
    public void clear() {
        list.clear();
        type = 0;
    }
}
