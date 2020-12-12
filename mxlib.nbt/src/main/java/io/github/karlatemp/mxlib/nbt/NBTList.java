/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTList.java
 */

package io.github.karlatemp.mxlib.nbt;

import java.util.AbstractList;

public abstract class NBTList<T extends NBTBase> extends AbstractList<T> implements NBTBase {
    public abstract T set(int index, T value);

    public abstract void add(int index, T value);

    public abstract T remove(int index);

    public abstract boolean setValue(int i, NBTBase base);

    public abstract boolean addValue(int i, NBTBase base);

    public abstract NBTList<T> clone();
}
