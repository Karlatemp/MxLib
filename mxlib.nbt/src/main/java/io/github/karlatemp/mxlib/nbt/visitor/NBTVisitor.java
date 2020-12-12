/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/02/15 24:50:41
 *
 * MXLib/mxlib.message/NBTVisitor.java
 */

package io.github.karlatemp.mxlib.nbt.visitor;

import io.github.karlatemp.mxlib.nbt.*;

public interface NBTVisitor {
    default void visitByte(String name, NBTTagByte value) {
    }

    default void visitByteArray(String name, NBTTagByteArray value) {
    }

    default void visitDouble(String name, NBTTagDouble value) {
    }

    default void visitEnd() {
    }

    default void visitFloat(String name, NBTTagFloat value) {
    }

    default void visitInt(String name, NBTTagInt value) {
    }

    default void visitIntArray(String name, NBTTagIntArray value) {
    }

    default void visitList(String name, NBTTagList value) {
    }

    default void visitLong(String name, NBTTagLong value) {
    }

    default void visitLongArray(String name, NBTTagLongArray value) {
    }

    default void visitShort(String name, NBTTagShort value) {
    }

    default void visitString(String name, NBTTagString value) {
    }

    default NBTVisitor visitCompound(String name, NBTTagCompound value) {
        return null;
    }
}
