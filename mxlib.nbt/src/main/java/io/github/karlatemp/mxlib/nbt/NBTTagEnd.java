/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTTagEnd.java
 */

package io.github.karlatemp.mxlib.nbt;

import io.github.karlatemp.mxlib.nbt.visitor.NBTVisitor;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagEnd implements NBTBase {
    @Override
    public void write(DataOutput out) throws IOException {
    }

    @Override
    public void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException {
        limiter.read(64L);
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitEnd();
    }

    @Override
    public byte getTypeId() {
        return 0;
    }

    @Override
    public String toString() {
        return "END";
    }

    @Override
    public NBTTagEnd clone() {
        return new NBTTagEnd();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NBTTagEnd;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
