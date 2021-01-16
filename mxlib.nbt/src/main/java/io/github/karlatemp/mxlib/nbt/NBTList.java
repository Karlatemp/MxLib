/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-nbt.main/NBTList.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
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
