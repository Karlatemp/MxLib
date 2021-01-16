/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-network-cs.test/TestReflectionSerializer.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package ncs;

import com.google.common.collect.Maps;
import io.github.karlatemp.mxlib.network.cs.serialize.PkgSerializable;
import io.github.karlatemp.mxlib.network.cs.serialize.SimpleSerializeContext;
import io.github.karlatemp.mxlib.network.cs.serialize.internal.ReflectSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

public class TestReflectionSerializer {
    @PkgSerializable
    public static class Stk {
        int valueInt = 12457;
        double valueDouble = 7874;
        long valueLong = 114514;
        String valueString = "Hello String";
        byte[] valueByteArray = new byte[]{1, 2, 3, 5, 4, 7, 9, 1, 4, 5, 1, 5, 2};
        short[] valueShortArray = new short[]{12, 487, 15, 87, 49, 4, 87, 8};
        int[] valueIntArray = new int[]{48248, 87, 984, 54879, 754, 687, 65};
        UUID rkUid = UUID.randomUUID();
        List<String> stringList = Arrays.asList("test", "test2", "test5");
        Map<String, String> stringMap = Maps.asMap(new HashSet<>(Arrays.asList(
                "key1", "k2awe0", "ewxawea", "ea5we7a8se7a7 "
        )), v -> "Value " + v + " ]");

        public static void equals(Stk s1, Stk s2) throws Throwable {
            for (Field f : Stk.class.getDeclaredFields()) {
                if (f.getType().isArray()) {
                    Class<?> type = f.getType();
                    if (type == byte[].class) {
                        Assertions.assertArrayEquals((byte[]) f.get(s1), (byte[]) f.get(s2), "Field: " + f);
                    } else if (type == int[].class) {
                        Assertions.assertArrayEquals((int[]) f.get(s1), (int[]) f.get(s2), "Field: " + f);
                    } else if (type == short[].class) {
                        Assertions.assertArrayEquals((short[]) f.get(s1), (short[]) f.get(s2), "Field: " + f);
                    } else if (type == boolean[].class) {
                        Assertions.assertArrayEquals((boolean[]) f.get(s1), (boolean[]) f.get(s2), "Field: " + f);
                    } else if (type == long[].class) {
                        Assertions.assertArrayEquals((long[]) f.get(s1), (long[]) f.get(s2), "Field: " + f);
                    } else if (type == char[].class) {
                        Assertions.assertArrayEquals((char[]) f.get(s1), (char[]) f.get(s2), "Field: " + f);
                    } else if (type == float[].class) {
                        Assertions.assertArrayEquals((float[]) f.get(s1), (float[]) f.get(s2), "Field: " + f);
                    } else {
                        Assertions.assertArrayEquals((Object[]) f.get(s1), (Object[]) f.get(s2), "Field: " + f);
                    }
                } else {
                    Assertions.assertEquals(f.get(s1), f.get(s2), "Field: " + f);
                }
            }
        }
    }

    @Test
    void testReflection() throws Throwable {
        Stk stk = new Stk();
        ByteBuf buf = Unpooled.directBuffer(1024);
        SimpleSerializeContext context = new SimpleSerializeContext(buf, SimpleSerializeContext.newSerializerMap());
        ReflectSerializer<Stk> serializer = new ReflectSerializer<>(Stk.class);
        serializer.serialize(stk, context);
        System.out.println(ByteBufUtil.hexDump(buf));
        Stk.equals(stk, serializer.deserialize(context));
    }
}
