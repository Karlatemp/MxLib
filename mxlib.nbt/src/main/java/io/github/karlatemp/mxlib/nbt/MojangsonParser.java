/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/MojangsonParser.java
 */

package io.github.karlatemp.mxlib.nbt;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.karlatemp.mxlib.nbt.util.TranslatableException;
import io.github.karlatemp.mxlib.nbt.util.TranslatableMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MojangsonParser {
    private StringReader reader;
    public static final SimpleCommandExceptionType a = new SimpleCommandExceptionType(new TranslatableMessage("argument.nbt.trailing"));
    public static final SimpleCommandExceptionType b = new SimpleCommandExceptionType(new TranslatableMessage("argument.nbt.expected.key"));
    public static final SimpleCommandExceptionType c = new SimpleCommandExceptionType(new TranslatableMessage("argument.nbt.expected.value"));
    public static final Dynamic2CommandExceptionType d = new Dynamic2CommandExceptionType((object, object1) -> new TranslatableMessage("argument.nbt.list.mixed", object, object1));
    public static final Dynamic2CommandExceptionType e = new Dynamic2CommandExceptionType((object, object1) -> new TranslatableMessage("argument.nbt.array.mixed", object, object1));
    public static final DynamicCommandExceptionType f = new DynamicCommandExceptionType(MojangsonParser::apply);
    private static final Pattern g = Pattern.compile("[-+]?(?:[0-9]+[.]|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?", Pattern.CASE_INSENSITIVE);
    private static final Pattern h = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?d", Pattern.CASE_INSENSITIVE);
    private static final Pattern i = Pattern.compile("[-+]?(?:[0-9]+[.]?|[0-9]*[.][0-9]+)(?:e[-+]?[0-9]+)?f", Pattern.CASE_INSENSITIVE);
    private static final Pattern j = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)b", Pattern.CASE_INSENSITIVE);
    private static final Pattern k = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)l", Pattern.CASE_INSENSITIVE);
    private static final Pattern l = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)s", Pattern.CASE_INSENSITIVE);
    private static final Pattern m = Pattern.compile("[-+]?(?:0|[1-9][0-9]*)");

    private MojangsonParser(StringReader safeStringReader) {
        this.reader = safeStringReader;
    }

    public static NBTTagCompound parse(String string) throws CommandSyntaxException {
        return new MojangsonParser(new StringReader(string)).parse();
    }

    private static Message apply(Object object) {
        return new TranslatableMessage("argument.nbt.array.invalid", object);
    }


    public NBTTagCompound parse() throws CommandSyntaxException {
        NBTTagCompound parsed = f();
        reader.skipWhitespace();
        if (reader.canRead())
            throw new TranslatableException("argument.nbt.trailing");
        return parsed;
    }

    protected String readKey() throws CommandSyntaxException {
        reader.skipWhitespace();
        if (reader.canRead())
            return reader.readString();
        throw b.createWithContext(reader);
    }

    protected NBTBase readValue() throws CommandSyntaxException {
        this.reader.skipWhitespace();
        int i = this.reader.getCursor();

        if (StringReader.isQuotedStringStart(this.reader.peek())) {
            return new NBTTagString(this.reader.readQuotedString());
        } else {
            String s = this.reader.readUnquotedString();

            if (s.isEmpty()) {
                this.reader.setCursor(i);
                throw MojangsonParser.c.createWithContext(this.reader);
            } else {
                return this.parseLiteral(s);
            }
        }
    }

    public NBTBase parseLiteral(String s) {
        try {
            if (MojangsonParser.i.matcher(s).matches())
                return new NBTTagFloat(Float.parseFloat(s.substring(0, s.length() - 1)));
            if (MojangsonParser.j.matcher(s).matches())
                return new NBTTagByte(Byte.parseByte(s.substring(0, s.length() - 1)));
            if (MojangsonParser.k.matcher(s).matches())
                return new NBTTagLong(Long.parseLong(s.substring(0, s.length() - 1)));
            if (MojangsonParser.l.matcher(s).matches())
                return new NBTTagShort(Short.parseShort(s.substring(0, s.length() - 1)));
            if (MojangsonParser.m.matcher(s).matches())
                return new NBTTagInt(Integer.parseInt(s));
            if (MojangsonParser.h.matcher(s).matches())
                return new NBTTagDouble(Double.parseDouble(s.substring(0, s.length() - 1)));
            if (MojangsonParser.g.matcher(s).matches())
                return new NBTTagDouble(Double.parseDouble(s));
            if ("true".equalsIgnoreCase(s))
                return new NBTTagByte((byte) 1);
            if ("false".equalsIgnoreCase(s))
                return new NBTTagByte((byte) 0);
        } catch (NumberFormatException ignore) {
        }
        return new NBTTagString(s);
    }

    public NBTBase d() throws CommandSyntaxException {
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw MojangsonParser.c.createWithContext(this.reader);
        } else {
            char c0 = this.reader.peek();
            return c0 == '{' ? this.f() : (c0 == '[' ? this.e() : this.readValue());
        }
    }

    protected NBTBase e() throws CommandSyntaxException {
        return this.reader.canRead(3) && !StringReader.isQuotedStringStart(this.reader.peek(1)) && this.reader.peek(2) == ';' ? this.parseArray() : this.g();
    }

    public NBTTagCompound f() throws CommandSyntaxException {
        this.a('{');
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        this.reader.skipWhitespace();

        while (this.reader.canRead() && this.reader.peek() != '}') {
            int i = this.reader.getCursor();
            String s = this.readKey();

            if (s.isEmpty()) {
                this.reader.setCursor(i);
                throw MojangsonParser.b.createWithContext(this.reader);
            }

            this.a(':');
            nbttagcompound.set(s, this.d());
            if (!this.i()) {
                break;
            }

            if (!this.reader.canRead()) {
                throw MojangsonParser.b.createWithContext(this.reader);
            }
        }

        this.a('}');
        return nbttagcompound;
    }

    private NBTBase g() throws CommandSyntaxException {
        this.a('[');
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw MojangsonParser.c.createWithContext(this.reader);
        } else {
            NBTTagList nbttaglist = new NBTTagList();
            byte b0 = -1;

            while (this.reader.peek() != ']') {
                int i = this.reader.getCursor();
                NBTBase nbtbase = this.d();
                byte b1 = nbtbase.getTypeId();

                if (b0 < 0) {
                    b0 = b1;
                } else if (b1 != b0) {
                    this.reader.setCursor(i);
                    throw MojangsonParser.d.createWithContext(this.reader, NBTBase.l(b1), NBTBase.l(b0));
                }

                nbttaglist.add(nbtbase);
                if (!this.i()) {
                    break;
                }

                if (!this.reader.canRead()) {
                    throw MojangsonParser.c.createWithContext(this.reader);
                }
            }

            this.a(']');
            return nbttaglist;
        }
    }

    public NBTBase parseArray() throws CommandSyntaxException {
        this.a('[');
        int i = this.reader.getCursor();
        char c0 = this.reader.read();

        this.reader.read();
        this.reader.skipWhitespace();
        if (!this.reader.canRead()) {
            throw MojangsonParser.c.createWithContext(this.reader);
        } else if (c0 == 'B') {
            return new NBTTagByteArray(this.a((byte) 7, (byte) 1));
        } else if (c0 == 'L') {
            return new NBTTagLongArray(this.a((byte) 12, (byte) 4));
        } else if (c0 == 'I') {
            return new NBTTagIntArray(this.a((byte) 11, (byte) 3));
        } else {
            this.reader.setCursor(i);
            throw MojangsonParser.f.createWithContext(this.reader, String.valueOf(c0));
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private <T extends Number> List<T> a(byte b0, byte b1) throws CommandSyntaxException {
        ArrayList arraylist = new ArrayList();

        while (true) {
            if (this.reader.peek() != ']') {
                int i = this.reader.getCursor();
                NBTBase nbtbase = this.d();
                byte b2 = nbtbase.getTypeId();

                if (b2 != b1) {
                    this.reader.setCursor(i);
                    throw MojangsonParser.e.createWithContext(this.reader, NBTBase.l(b2), NBTBase.l(b0));
                }

                if (b1 == 1) {
                    arraylist.add(((NBTNumber) nbtbase).asByte());
                } else if (b1 == 4) {
                    arraylist.add(((NBTNumber) nbtbase).asLong());
                } else {
                    arraylist.add(((NBTNumber) nbtbase).asInt());
                }

                if (this.i()) {
                    if (!this.reader.canRead()) {
                        throw MojangsonParser.c.createWithContext(this.reader);
                    }
                    continue;
                }
            }

            this.a(']');
            return (List<T>) arraylist;
        }
    }

    private boolean i() {
        this.reader.skipWhitespace();
        if (this.reader.canRead() && this.reader.peek() == ',') {
            this.reader.skip();
            this.reader.skipWhitespace();
            return true;
        } else {
            return false;
        }
    }

    private void a(char c0) throws CommandSyntaxException {
        this.reader.skipWhitespace();
        this.reader.expect(c0);
    }
}
