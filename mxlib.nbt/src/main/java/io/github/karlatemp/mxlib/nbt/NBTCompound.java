/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTCompound.java
 */

package io.github.karlatemp.mxlib.nbt;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface NBTCompound extends NBTBase {
    int size();

    @Contract(pure = true)
    @NotNull
    Set<String> getKeys();

    @Nullable
    NBTBase set(String key, NBTBase value);

    void setByte(String key, byte value);

    void setShort(String key, short value);

    void setInt(String s, int i);

    void setLong(String s, long i);

    void setUUID(String s, UUID uuid);

    UUID getUUID(String s);

    boolean hasUUID(String s);

    void setFloat(String s, float f);

    void setDouble(String s, double d0);

    void setString(String s, String s1);

    void setByteArray(String s, byte[] abyte);

    void setIntArray(String s, int[] aint);

    void setIntArray(String s, List<Integer> list);

    void setLongArray(String s, long[] along);

    void setLongArray(String s, List<Long> list);

    void setBoolean(String s, boolean flag);

    @Nullable
    NBTBase get(String s);

    byte getType(String s);

    boolean hasKey(String s);

    boolean hasKeyOfType(String s, int i);

    byte getByte(String s);

    short getShort(String s);

    int getInt(String s);

    long getLong(String s);

    float getFloat(String s);

    double getDouble(String s);

    String getString(String s);

    byte[] getByteArray(String s);

    int[] getIntArray(String s);

    long[] getLongArray(String s);

    NBTCompound getCompound(String s);

    NBTTagList getList(String s, int i);

    boolean getBoolean(String s);

    void remove(String s);

    boolean isEmpty();

    @NotNull
    @Contract(pure = true)
    NBTCompound clone();
}
