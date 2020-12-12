/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTTagCompound.java
 */

package io.github.karlatemp.mxlib.nbt;

import io.github.karlatemp.mxlib.nbt.visitor.NBTVisitor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class NBTTagCompound implements NBTCompound {
    private static final Pattern g = Pattern.compile("[A-Za-z0-9._+-]+");
    private final Map<String, NBTBase> map = new HashMap<>();

    @Override
    public void write(DataOutput out) throws IOException {
        for (Map.Entry<String, NBTBase> entry : map.entrySet()) {
            write(entry.getKey(), entry.getValue(), out);
        }
        out.writeByte(0);
    }

    private static void write(String name, NBTBase nbtbase, DataOutput out) throws IOException {
        out.writeByte(nbtbase.getTypeId());
        if (nbtbase.getTypeId() != 0) {
            out.writeUTF(name);
            nbtbase.write(out);
        }
    }

    @Override
    public void accept(String name, NBTVisitor visitor) {
        final NBTVisitor child = visitor.visitCompound(name, this);
        if (child != null) {
            for (Map.Entry<String, NBTBase> entry : map.entrySet()) {
                entry.getValue().accept(entry.getKey(), child);
            }
            child.visitEnd();
        }
    }

    @Override
    public void load(DataInput input, int depth, NBTReadLimiter limiter) throws IOException {
        limiter.read(384L);
        if (depth > 512) {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        map.clear();
        byte b;
        while ((b = input.readByte()) != 0) {
            String name = input.readUTF();
            limiter.read((long) (224 + 16 * name.length()));
            NBTBase nase = read(b, name, input, depth + 1, limiter);

            if (this.map.put(name, nase) != null) {
                limiter.read(288L);
            }
        }
    }

    static NBTBase read(byte id, String name, DataInput input, int i, NBTReadLimiter limiter) throws IOException {
        NBTBase base = NBTBase.createTag(id);
        try {
            base.load(input, i, limiter);
        } catch (IOException ioe) {
            throw new IOException("Error in reading tag[" + name + "]", ioe);
        }
        return base;
    }

    @Override
    public byte getTypeId() {
        return 10;
    }

    public int size() {
        return map.size();
    }

    @Contract(pure = true)
    @NotNull
    public Set<String> getKeys() {
        return map.keySet();
    }


    @Nullable
    public NBTBase set(String key, NBTBase value) {
        return map.put(key, value);
    }

    public void setByte(String key, byte value) {
        set(key, new NBTTagByte(value));
    }

    public void setShort(String key, short value) {
        set(key, new NBTTagShort(value));
    }

    public void setInt(String s, int i) {
        this.map.put(s, new NBTTagInt(i));
    }

    public void setLong(String s, long i) {
        this.map.put(s, new NBTTagLong(i));
    }

    public void setUUID(String s, UUID uuid) {
        this.setLong(s + "Most", uuid.getMostSignificantBits());
        this.setLong(s + "Least", uuid.getLeastSignificantBits());
    }

    public UUID getUUID(String s) {
        return new UUID(this.getLong(s + "Most"), this.getLong(s + "Least"));
    }

    public boolean hasUUID(String s) {
        return this.hasKeyOfType(s + "Most", 99) && this.hasKeyOfType(s + "Least", 99);
    }

    public void setFloat(String s, float f) {
        this.map.put(s, new NBTTagFloat(f));
    }

    public void setDouble(String s, double d0) {
        this.map.put(s, new NBTTagDouble(d0));
    }

    public void setString(String s, String s1) {
        this.map.put(s, new NBTTagString(s1));
    }

    public void setByteArray(String s, byte[] abyte) {
        this.map.put(s, new NBTTagByteArray(abyte));
    }

    public void setIntArray(String s, int[] aint) {
        this.map.put(s, new NBTTagIntArray(aint));
    }

    public void setIntArray(String s, List<Integer> list) {
        this.map.put(s, new NBTTagIntArray(list));
    }

    public void setLongArray(String s, long[] along) {
        this.map.put(s, new NBTTagLongArray(along));
    }

    public void setLongArray(String s, List<Long> list) {
        this.map.put(s, new NBTTagLongArray(list));
    }

    public void setBoolean(String s, boolean flag) {
        this.setByte(s, (byte) (flag ? 1 : 0));
    }

    @Nullable
    public NBTBase get(String s) {
        return this.map.get(s);
    }

    public byte getType(String s) {
        NBTBase nbtbase = this.map.get(s);

        return nbtbase == null ? 0 : nbtbase.getTypeId();
    }

    public boolean hasKey(String s) {
        return this.map.containsKey(s);
    }

    public static boolean hasKeyOfType(NBTCompound compound, String s, int i) {
        byte b0 = compound.getType(s);

        return b0 == i || (i == 99 && (b0 == 1 || b0 == 2 || b0 == 3 || b0 == 4 || b0 == 5 || b0 == 6));
    }

    public boolean hasKeyOfType(String s, int i) {
        return hasKeyOfType(this, s, i);
    }

    public byte getByte(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber) this.map.get(s)).asByte();
            }
        } catch (ClassCastException ignore) {
        }

        return 0;
    }

    public short getShort(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber) this.map.get(s)).asShort();
            }
        } catch (ClassCastException ignore) {
        }

        return 0;
    }

    public int getInt(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber) this.map.get(s)).asInt();
            }
        } catch (ClassCastException ignore) {
        }

        return 0;
    }

    public long getLong(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber) this.map.get(s)).asLong();
            }
        } catch (ClassCastException ignore) {
        }

        return 0L;
    }

    public float getFloat(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber) this.map.get(s)).asFloat();
            }
        } catch (ClassCastException ignore) {
        }

        return 0.0F;
    }

    public double getDouble(String s) {
        try {
            if (this.hasKeyOfType(s, 99)) {
                return ((NBTNumber) this.map.get(s)).asDouble();
            }
        } catch (ClassCastException ignore) {
        }

        return 0.0D;
    }

    public String getString(String s) {
        try {
            if (this.hasKeyOfType(s, 8)) {
                return this.map.get(s).asString();
            }
        } catch (ClassCastException ignore) {
        }

        return "";
    }

    public byte[] getByteArray(String s) {
        try {
            if (this.hasKeyOfType(s, 7)) {
                return ((NBTTagByteArray) this.map.get(s)).getBytes();
            }
        } catch (ClassCastException ignore) {
        }

        return new byte[0];
    }

    public int[] getIntArray(String s) {
        try {
            if (this.hasKeyOfType(s, 11)) {
                return ((NBTTagIntArray) this.map.get(s)).getInts();
            }
        } catch (ClassCastException ignore) {
        }

        return new int[0];
    }

    public long[] getLongArray(String s) {
        try {
            if (this.hasKeyOfType(s, 12)) {
                return ((NBTTagLongArray) this.map.get(s)).getLongs();
            }
        } catch (ClassCastException ignore) {
        }

        return new long[0];
    }

    public NBTTagCompound getCompound(String s) {
        try {
            if (this.hasKeyOfType(s, 10)) {
                return (NBTTagCompound) this.map.get(s);
            }
        } catch (ClassCastException ignore) {
        }

        return new NBTTagCompound();
    }

    public NBTTagList getList(String s, int i) {
        try {
            if (this.getType(s) == 9) {
                NBTTagList nbttaglist = (NBTTagList) this.map.get(s);

                if (!nbttaglist.isEmpty() && nbttaglist.getType() != i) {
                    return new NBTTagList();
                }

                return nbttaglist;
            }
        } catch (ClassCastException ignore) {
        }

        return new NBTTagList();
    }

    public boolean getBoolean(String s) {
        return this.getByte(s) != 0;
    }

    public void remove(String s) {
        this.map.remove(s);
    }

    public static String s(String s) {
        return NBTTagCompound.g.matcher(s).matches() ? s : NBTTagString.a(s);
    }

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder("{");
        Collection<String> collection = this.map.keySet();
        String s;

        for (Iterator iterator = ((Collection) collection).iterator();
             iterator.hasNext();
             stringbuilder.append(s(s)).append(':').append(this.map.get(s))) {
            s = (String) iterator.next();
            if (stringbuilder.length() != 1) {
                stringbuilder.append(',');
            }
        }

        return stringbuilder.append('}').toString();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @NotNull
    public NBTTagCompound clone() {
        NBTTagCompound cp = new NBTTagCompound();
        cp.map.putAll(map);
        return cp;
    }

    public boolean equals(Object object) {
        return this == object || object instanceof NBTTagCompound && Objects.equals(this.map, ((NBTTagCompound) object).map);
    }

    public int hashCode() {
        return this.map.hashCode();
    }
}
