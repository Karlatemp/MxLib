/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-nbt.main/NBTTagEnd.java
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
