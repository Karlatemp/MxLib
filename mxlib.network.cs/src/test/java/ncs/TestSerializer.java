package ncs;

import io.github.karlatemp.mxlib.network.cs.serialize.PacketSerializer;
import io.github.karlatemp.mxlib.network.cs.serialize.SimpleSerializeContext;
import io.github.karlatemp.mxlib.network.cs.serialize.internal.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class TestSerializer {
    @Test
    void testIntArraySerializer() {
        IntArraySerializer serializer = new IntArraySerializer();
        int[] test = new int[]{0, 1, 2, 3, 4, 5};
        ByteBuf buf = Unpooled.directBuffer(test.length * Integer.BYTES);
        PacketSerializer.SerializeContext context = new SimpleSerializeContext(
                buf, Collections.emptyMap()
        );
        serializer.serialize(test, context);
        Assertions.assertArrayEquals(test, serializer.deserialize(context));


        ByteBuf buf2 = Unpooled.buffer(buf.capacity());
        PacketSerializer.SerializeContext context2 = new SimpleSerializeContext(
                buf2, Collections.emptyMap()
        );
        serializer.serialize(test, context2);
        Assertions.assertArrayEquals(test, serializer.deserialize(context2));
    }

    @Test
    void testShortArraySerializer() {
        ShortArraySerializer serializer = new ShortArraySerializer();
        short[] test = new short[]{0, 1, 2, 3, 4, 5};
        ByteBuf buf = Unpooled.directBuffer(test.length * Short.BYTES);
        PacketSerializer.SerializeContext context = new SimpleSerializeContext(
                buf, Collections.emptyMap()
        );
        serializer.serialize(test, context);
        Assertions.assertArrayEquals(test, serializer.deserialize(context));


        ByteBuf buf2 = Unpooled.buffer(buf.capacity());
        PacketSerializer.SerializeContext context2 = new SimpleSerializeContext(
                buf2, Collections.emptyMap()
        );
        serializer.serialize(test, context2);
        Assertions.assertArrayEquals(test, serializer.deserialize(context2));
    }

    @Test
    void testFloatArraySerializer() {
        FloatArraySerializer serializer = new FloatArraySerializer();
        float[] test = new float[]{0, 1, 2, 3, 4, 5};
        ByteBuf buf = Unpooled.directBuffer(test.length * Short.BYTES);
        PacketSerializer.SerializeContext context = new SimpleSerializeContext(
                buf, Collections.emptyMap()
        );
        serializer.serialize(test, context);
        Assertions.assertArrayEquals(test, serializer.deserialize(context));


        ByteBuf buf2 = Unpooled.buffer(buf.capacity());
        PacketSerializer.SerializeContext context2 = new SimpleSerializeContext(
                buf2, Collections.emptyMap()
        );
        serializer.serialize(test, context2);
        Assertions.assertArrayEquals(test, serializer.deserialize(context2));
    }

    @Test
    void testDoubleArraySerializer() {
        DoubleArraySerializer serializer = new DoubleArraySerializer();
        double[] test = new double[]{0, 1, 2, 3, 4, 5};
        ByteBuf buf = Unpooled.directBuffer(test.length * Short.BYTES);
        PacketSerializer.SerializeContext context = new SimpleSerializeContext(
                buf, Collections.emptyMap()
        );
        serializer.serialize(test, context);
        Assertions.assertArrayEquals(test, serializer.deserialize(context));


        ByteBuf buf2 = Unpooled.buffer(buf.capacity());
        PacketSerializer.SerializeContext context2 = new SimpleSerializeContext(
                buf2, Collections.emptyMap()
        );
        serializer.serialize(test, context2);
        Assertions.assertArrayEquals(test, serializer.deserialize(context2));
    }

    @Test
    void testCharArraySerializer() {
        CharArraySerializer serializer = new CharArraySerializer();
        char[] test = "new short[]{0, 1, 2, 3, 4, 5}".toCharArray();
        ByteBuf buf = Unpooled.directBuffer(test.length * Character.BYTES);
        PacketSerializer.SerializeContext context = new SimpleSerializeContext(
                buf, Collections.emptyMap()
        );
        serializer.serialize(test, context);
        Assertions.assertArrayEquals(test, serializer.deserialize(context));


        ByteBuf buf2 = Unpooled.buffer(buf.capacity());
        PacketSerializer.SerializeContext context2 = new SimpleSerializeContext(
                buf2, Collections.emptyMap()
        );
        serializer.serialize(test, context2);
        Assertions.assertArrayEquals(test, serializer.deserialize(context2));
    }

    @Test
    void testLongArraySerializer() {
        LongArraySerializer serializer = new LongArraySerializer();
        long[] test = new long[]{0, 1, 2, 3, 4, 5};
        ByteBuf buf = Unpooled.directBuffer(test.length * Long.BYTES);
        PacketSerializer.SerializeContext context = new SimpleSerializeContext(
                buf, Collections.emptyMap()
        );
        serializer.serialize(test, context);
        Assertions.assertArrayEquals(test, serializer.deserialize(context));


        ByteBuf buf2 = Unpooled.buffer(buf.capacity());
        PacketSerializer.SerializeContext context2 = new SimpleSerializeContext(
                buf2, Collections.emptyMap()
        );
        serializer.serialize(test, context2);
        Assertions.assertArrayEquals(test, serializer.deserialize(context2));
    }

    @Test
    void testBooleanArraySerializer() {
        BooleanArraySerializer serializer = new BooleanArraySerializer();
        boolean[] test = new boolean[]{
                true, true, false, true,
                true, false, true, false,
                true, true
        };
        ByteBuf buf = Unpooled.directBuffer(test.length * Character.BYTES);
        PacketSerializer.SerializeContext context = new SimpleSerializeContext(
                buf, Collections.emptyMap()
        );
        serializer.serialize(test, context);
        Assertions.assertArrayEquals(test, serializer.deserialize(context));


        ByteBuf buf2 = Unpooled.buffer(buf.capacity());
        PacketSerializer.SerializeContext context2 = new SimpleSerializeContext(
                buf2, Collections.emptyMap()
        );
        serializer.serialize(test, context2);
        Assertions.assertArrayEquals(test, serializer.deserialize(context2));
    }
}
