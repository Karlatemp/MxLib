package io.github.karlatemp.mxlib.network.cs.serialize.internal;

import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class StringSerializer implements PacketSerializer<String> {
    @Override
    public void serialize(String value, SerializeContext context) {
        ByteBuf buf = context.getBuf();
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        buf.ensureWritable(Integer.BYTES + bytes.length);
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    @Override
    public @NotNull String deserialize(SerializeContext context) {
        ByteBuf buf = context.getBuf();
        int size = buf.readInt();
        buf.skipBytes(size);
        return buf.toString(buf.readerIndex() - size, size, StandardCharsets.UTF_8);
    }

    @Override
    public @NotNull Class<String[]> arrayType() {
        return String[].class;
    }
}
