/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTCompressedStreamTools.java
 */

package io.github.karlatemp.mxlib.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTCompressedStreamTools {
    public static NBTTagCompound loadCompound(
            DataInput input,
            NBTReadLimiter limiter) throws IOException {
        NBTBase base = readBase(
                input, 0, limiter
        );
        if (base instanceof NBTTagCompound) {
            return (NBTTagCompound) base;
        }
        throw new IOException("Root tag must be a named compound tag");
    }

    public static void write(NBTTagCompound compound, DataOutput out) throws IOException {
        write((NBTBase) compound, out);
    }

    public static void write(NBTBase base, DataOutput out) throws IOException {
        out.writeByte(base.getTypeId());
        if (base.getTypeId() != 0) {
            out.writeUTF("");
            base.write(out);
        }
    }

    public static NBTBase readBase(
            DataInput input, int depth, NBTReadLimiter limiter) throws IOException {
        byte id = input.readByte();
        if (id == 0) return new NBTTagEnd();
        input.readUTF();
        final NBTBase tag = NBTBase.createTag(id);
        tag.load(input, depth, limiter);
        return tag;
    }
}
