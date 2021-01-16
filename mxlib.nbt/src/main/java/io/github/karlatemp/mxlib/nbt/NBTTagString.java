/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-nbt.main/NBTTagString.java
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
import java.util.Objects;

public class NBTTagString implements NBTBase {
    private String data;

    public NBTTagString() {
        this("");
    }

    public NBTTagString(String s) {
        Objects.requireNonNull(s, "Null string not allowed");
        this.data = s;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(data);
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        visitor.visitString(name, this);
    }

    @Override
    public void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException {
        limiter.read(288L);
        data = input.readUTF();
        limiter.read(16 * data.length());
    }

    @Override
    public byte getTypeId() {
        return 8;
    }

    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagString && Objects.equals(this.data, ((NBTTagString) object).data);
    }

    @Override
    public NBTTagString clone() {
        return new NBTTagString(data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @Override
    public String asString() {
        return data;
    }

    @Override
    public String toString() {
        return a(data);
    }

    public static String a(String s) {
        StringBuilder stringbuilder = new StringBuilder(" ");
        int i = 0;

        for (int j = 0; j < s.length(); ++j) {
            char c0 = s.charAt(j);

            if (c0 == '\\') {
                stringbuilder.append('\\');
            } else if (c0 == '"' || c0 == '\'') {
                if (i == 0) {
                    i = c0 == '"' ? 39 : 34;
                }

                if (i == c0) {
                    stringbuilder.append('\\');
                }
            }

            stringbuilder.append(c0);
        }

        if (i == 0) {
            i = 34;
        }

        stringbuilder.setCharAt(0, (char) i);
        stringbuilder.append((char) i);
        return stringbuilder.toString();
    }
}
