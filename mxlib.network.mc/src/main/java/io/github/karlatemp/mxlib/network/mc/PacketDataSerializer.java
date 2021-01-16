/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/PacketDataSerializer.java
 */

package io.github.karlatemp.mxlib.network.mc;

import io.github.karlatemp.mxlib.nbt.NBTCompressedStreamTools;
import io.github.karlatemp.mxlib.nbt.NBTReadLimiter;
import io.github.karlatemp.mxlib.nbt.NBTTagCompound;
import io.github.karlatemp.mxlib.reflect.WrappedClassImplements;
import io.github.karlatemp.unsafeaccessor.Root;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public abstract class PacketDataSerializer extends ByteBuf {
    private static final Class<? extends PacketDataSerializer> implementClass;
    private static final MethodHandle newInstance;

    static {
        byte[] data = WrappedClassImplements.doImplement(PacketDataSerializer.class).toByteArray();
        implementClass = Root.getUnsafe().defineClass(null, data, 0, data.length,PacketDataSerializer.class.getClassLoader(),  PacketDataSerializer.class.getProtectionDomain())
                .asSubclass(PacketDataSerializer.class);
        MethodHandles.Lookup lk = Root.getTrusted();
        MethodHandle mh;
        try {
            mh = lk.findConstructor(implementClass, MethodType.methodType(void.class, ByteBuf.class));
        } catch (Exception e) {
            try {
                mh = lk.findConstructor(implementClass, MethodType.methodType(void.class, WrappedClassImplements.getRootAbstractClass(PacketDataSerializer.class)));
            } catch (NoSuchMethodException | IllegalAccessException e1) {
                try {
                    mh = lk.findConstructor(implementClass, MethodType.methodType(void.class));
                    mh = MethodHandles.dropArguments(mh, 1, Object.class);
                } catch (NoSuchMethodException | IllegalAccessException e2) {
                    ExceptionInInitializerError e7 = new ExceptionInInitializerError(e2);
                    e7.addSuppressed(e1);
                    e7.addSuppressed(e);
                    throw e7;
                }
            }
        }
        newInstance = mh;
    }

    public static PacketDataSerializer fromByteBuf(ByteBuf buf) {
        if (buf instanceof PacketDataSerializer)
            return (PacketDataSerializer) buf;
        try {
            return (PacketDataSerializer) newInstance.invoke(buf);
        } catch (Throwable throwable) {
            Root.getUnsafe().throwException(throwable);
            throw new AssertionError();
        }
    }

    private final ByteBuf a;

    protected PacketDataSerializer(ByteBuf bytebuf) {
        this.a = bytebuf;
    }

    public static int $unknown_a(int i) {
        for (int j = 1; j < 5; ++j) {
            if ((i & -1 << j * 7) == 0) {
                return j;
            }
        }

        return 5;
    }

    public PacketDataSerializer writeByteArray(byte[] abyte) {
        this.writeVarInt(abyte.length);
        this.writeBytes(abyte);
        return this;
    }

    @Override
    public final ByteBuf unwrap() {
        return a;
    }
    /*public MinecraftKey readMinecraftKey() {
        return new MinecraftKey(readString(32767));
    }

    public PacketDataSerializer writeMinecraftKey(MinecraftKey key) {
        writeString(key.toString());
        return this;
    }*/

    public byte[] readByteArray() {
        return this.readByteArray(this.readableBytes());
    }

    public byte[] readByteArray(int allows) {
        int j = this.readVarInt();

        if (j > allows) {
            throw new DecoderException("ByteArray with size " + j + " is bigger than allowed " + allows);
        } else {
            byte[] abyte = new byte[j];

            this.readBytes(abyte);
            return abyte;
        }
    }

    public PacketDataSerializer writeVarIntArray(int[] aint) {
        this.writeVarInt(aint.length);
        int[] aint1 = aint;
        int i = aint.length;

        for (int j = 0; j < i; ++j) {
            int k = aint1[j];

            this.writeVarInt(k);
        }

        return this;
    }

    public int[] readVarIntArray() {
        return this.readVarIntArray(this.readableBytes());
    }

    public int[] readVarIntArray(int allowed) {
        int j = this.readVarInt();

        if (j > allowed) {
            throw new DecoderException("VarIntArray with size " + j + " is bigger than allowed " + allowed);
        } else {
            int[] aint = new int[j];

            for (int k = 0; k < aint.length; ++k) {
                aint[k] = this.readVarInt();
            }

            return aint;
        }
    }

    public PacketDataSerializer writeLongArray(long[] along) {
        this.writeVarInt(along.length);
        long[] along1 = along;
        int i = along.length;

        for (int j = 0; j < i; ++j) {
            long k = along1[j];

            this.writeLong(k);
        }

        return this;
    }

    public <T extends Enum<T>> T readEnum(Class<T> oclass) {
        return (oclass.getEnumConstants())[this.readVarInt()];
    }

    public PacketDataSerializer writeEnum(Enum<?> oenum) {
        return this.writeVarInt(oenum.ordinal());
    }

    public int readVarInt() {
        int i = 0;
        int j = 0;

        byte b0;

        do {
            b0 = this.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public long readVarLong() {
        long i = 0L;
        int j = 0;

        byte b0;

        do {
            b0 = this.readByte();
            i |= (long) (b0 & 127) << j++ * 7;
            if (j > 10) {
                throw new RuntimeException("VarLong too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public PacketDataSerializer writeUUID(UUID uuid) {
        this.writeLong(uuid.getMostSignificantBits());
        this.writeLong(uuid.getLeastSignificantBits());
        return this;
    }

    public UUID readUUID() {
        return new UUID(this.readLong(), this.readLong());
    }

    public PacketDataSerializer writeVarInt(int i) {
        while ((i & -128) != 0) {
            this.writeByte(i & 127 | 128);
            i >>>= 7;
        }

        this.writeByte(i);
        return this;
    }

    public PacketDataSerializer writeVarLong(long i) {
        while ((i & -128L) != 0L) {
            this.writeByte((int) (i & 127L) | 128);
            i >>>= 7;
        }

        this.writeByte((int) i);
        return this;
    }

    public PacketDataSerializer writeNBT(@Nullable NBTTagCompound nbttagcompound) {
        if (nbttagcompound == null) {
            this.writeByte(0);
        } else {
            try {
                NBTCompressedStreamTools.write(nbttagcompound, new ByteBufOutputStream(this));
            } catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }

        return this;
    }

    @Nullable
    public NBTTagCompound readNBT() {
        int i = this.readerIndex();
        byte b0 = this.readByte();

        if (b0 == 0) {
            return null;
        } else {
            this.readerIndex(i);

            try {
                return NBTCompressedStreamTools.loadCompound(new ByteBufInputStream(this), new NBTReadLimiter(2097152L));
            } catch (IOException ioexception) {
                throw new EncoderException(ioexception);
            }
        }
    }

    /*
        public PacketDataSerializer a(ItemStack itemstack) {
            if (itemstack.isEmpty()) {
                this.writeBoolean(false);
            } else {
                this.writeBoolean(true);
                Item item = itemstack.getItem();

                this.writeVarInt(Item.getId(item));
                this.writeByte(itemstack.getCount());
                NBTTagCompound nbttagcompound = null;

                if (item.usesDurability() || item.m()) {
                    nbttagcompound = itemstack.getTag();
                }

                this.a(nbttagcompound);
            }

            return this;
        }

        public ItemStack m() {
            if (!this.readBoolean()) {
                return ItemStack.a;
            } else {
                int i = this.i();
                byte b0 = this.readByte();
                ItemStack itemstack = new ItemStack(Item.getById(i), b0);

                itemstack.setTag(this.l());
                return itemstack;
            }
        }*/

    public String readString() {
        return readString(32767);
    }

    public String readString(int i) {
        int j = this.readVarInt();

        if (j > i * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + i * 4 + ")");
        } else if (j < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            String s = this.toString(this.readerIndex(), j, StandardCharsets.UTF_8);

            this.readerIndex(this.readerIndex() + j);
            if (s.length() > i) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + j + " > " + i + ")");
            } else {
                return s;
            }
        }
    }

    public PacketDataSerializer writeString(String s) {
        return this.writeString(s, 32767);
    }

    public PacketDataSerializer writeString(String s, int allowed) {
        byte[] abyte = s.getBytes(StandardCharsets.UTF_8);

        if (abyte.length > allowed) {
            throw new EncoderException("String too big (was " + abyte.length + " bytes encoded, max " + allowed + ")");
        } else {
            this.writeVarInt(abyte.length);
            this.writeBytes(abyte);
            return this;
        }
    }

    public Date readDate() {
        return new Date(this.readLong());
    }

    public PacketDataSerializer writeDate(Date date) {
        this.writeLong(date.getTime());
        return this;
    }

    @Override
    public String toString() {
        return a.toString();
    }

    @Override
    public String toString(Charset charset) {
        return a.toString(charset);
    }

    @Override
    public String toString(int index, int length, Charset charset) {
        return a.toString(index, length, charset);
    }
}
