/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.main/BooleanArraySerializer.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.BitSet;

public class BooleanArraySerializer implements PacketSerializer<boolean[]> {
    private static final BitSetSerializer DELEGATE = new BitSetSerializer();

    @Override
    public void serialize(boolean[] value, SerializeContext context) {
        int bits = value.length;
        BitSet set = new BitSet(bits);
        for (int i = 0; i < bits; i++) {
            set.set(i, value[i]);
        }
        DELEGATE.serialize(set, context);
    }

    @Override
    public boolean @NotNull [] deserialize(SerializeContext context) {
        BitSet set = DELEGATE.deserialize(context);
        boolean[] results = new boolean[set.length()];
        int ln = results.length;
        for (int i = 0; i < ln; i++) {
            results[i] = set.get(i);
        }
        return results;
    }

    @Override
    public @NotNull Class<boolean[][]> arrayType() {
        return boolean[][].class;
    }
}
