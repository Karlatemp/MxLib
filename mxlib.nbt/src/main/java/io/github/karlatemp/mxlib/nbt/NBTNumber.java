/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-nbt.main/NBTNumber.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.nbt;

public interface NBTNumber extends NBTBase {
    long asLong();

    int asInt();

    short asShort();

    byte asByte();

    double asDouble();

    float asFloat();

    Number getValue();
}
