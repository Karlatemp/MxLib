/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTPrinter.java
 */

package io.github.karlatemp.mxlib.nbt.visitor;

import io.github.karlatemp.mxlib.nbt.*;
import io.github.karlatemp.mxlib.utils.LineWriter;
import org.jetbrains.annotations.NotNull;

public class NBTPrinter implements NBTVisitor {
    private final LineWriter writer;
    private String prefix = "";
    private String prefixEnd = "";

    public NBTPrinter(@NotNull LineWriter writer) {
        this.writer = writer;
    }


    public void visit(String name, NBTBase base) {
        writer.println(prefix + v(name) + " " + base);
    }

    private static String v(String a) {
        if (a == null) return "";
        return NBTTagCompound.s(a) + ":";
    }

    @Override
    public void visitString(String name, NBTTagString value) {
        visit(name, value);
    }

    @Override
    public void visitByte(String name, NBTTagByte value) {
        visit(name, value);
    }

    @Override
    public void visitByteArray(String name, NBTTagByteArray value) {
        visit(name, value);
    }

    @Override
    public void visitDouble(String name, NBTTagDouble value) {
        visit(name, value);
    }

    @Override
    public void visitEnd() {
        writer.println(prefixEnd + "}");
    }

    @Override
    public void visitFloat(String name, NBTTagFloat value) {
        visit(name, value);
    }

    @Override
    public void visitInt(String name, NBTTagInt value) {
        visit(name, value);
    }

    @Override
    public void visitIntArray(String name, NBTTagIntArray value) {
        visit(name, value);
    }

    @Override
    public void visitList(String name, NBTTagList value) {
        if (value.isEmpty()) {
            writer.println(prefix + v(name) + " []");
            return;
        }
        writer.println(prefix + v(name) + " [");
        NBTPrinter printer = new NBTPrinter(writer);
        printer.prefix = prefix + "\t";
        for (NBTBase base : value) {
            base.accept(null, printer);
        }
        writer.println(prefix + "]");
    }

    @Override
    public void visitLong(String name, NBTTagLong value) {
        visit(name, value);
    }

    @Override
    public void visitLongArray(String name, NBTTagLongArray value) {
        visit(name, value);
    }

    @Override
    public void visitShort(String name, NBTTagShort value) {
        visit(name, value);
    }

    @Override
    public NBTVisitor visitCompound(String name, NBTTagCompound value) {
        writer.println(prefix + v(name) + "{");
        NBTPrinter printer = new NBTPrinter(writer);
        printer.prefix = prefix + "\t";
        printer.prefixEnd = prefix;
        return printer;
    }
}
