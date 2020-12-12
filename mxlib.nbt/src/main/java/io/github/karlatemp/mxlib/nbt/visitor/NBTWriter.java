/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTWriter.java
 */
package io.github.karlatemp.mxlib.nbt.visitor;

import io.github.karlatemp.mxlib.nbt.*;

import java.io.DataOutput;
import java.io.IOError;
import java.io.IOException;

public class NBTWriter implements NBTVisitor {
    private final DataOutput writer;

    public NBTWriter(DataOutput writer) {
        this.writer = writer;
    }

    public void visit(String name, NBTBase base) {
        try {
            writer.write(base.getTypeId());
            if (base.getTypeId() != 0) {
                writer.writeUTF(name);
                base.write(writer);
            }
        } catch (IOException e) {
            throw new IOError(e);
        }
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
        try {
            writer.write(0);
        } catch (IOException e) {
            throw new IOError(e);
        }
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
        visit(name, value);
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
        visit(name, value);
        return this;
    }
}
