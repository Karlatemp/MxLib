package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.BitSet;

public class BitSetSerializer implements PacketSerializer<BitSet> {
    private static final LongArraySerializer DELEGATE = new LongArraySerializer();

    @Override
    public void serialize(BitSet value, SerializeContext context) {
        DELEGATE.serialize(value.toLongArray(), context);
    }

    @Override
    public @NotNull BitSet deserialize(SerializeContext context) {
        return BitSet.valueOf(DELEGATE.deserialize(context));
    }

    @Override
    public @NotNull Class<BitSet[]> arrayType() {
        return BitSet[].class;
    }
}
