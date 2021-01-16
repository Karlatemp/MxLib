package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import io.github.karlatemp.mxlib.utils.Toolkit;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MapEntrySerializer<K, V> implements PacketSerializer<Map.Entry<K, V>> {
    private final PacketSerializer<K> keySerializer;
    private final PacketSerializer<V> valueSerializer;

    public MapEntrySerializer(
            PacketSerializer<K> keySerializer,
            PacketSerializer<V> valueSerializer
    ) {
        this.keySerializer = keySerializer;
        this.valueSerializer = valueSerializer;
    }

    @Override
    public void serialize(Map.Entry<K, V> value, SerializeContext context) {
        keySerializer.serialize(value.getKey(), context);
        valueSerializer.serialize(value.getValue(), context);
    }

    @Override
    public @NotNull Map.Entry<K, V> deserialize(SerializeContext context) {
        return Toolkit.entry(
                keySerializer.deserialize(context),
                valueSerializer.deserialize(context)
        );
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Class<Map.Entry<K, V>[]> arrayType() {
        return (Class<Map.Entry<K,V>[]>) Map.Entry[].class;
    }
}
