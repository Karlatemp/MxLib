/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.main/AccessibleByteArrayInputStream.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.utils;

import java.io.ByteArrayInputStream;

public class AccessibleByteArrayInputStream extends ByteArrayInputStream {
    public AccessibleByteArrayInputStream(byte[] buf) {
        super(buf);
    }

    public AccessibleByteArrayInputStream(byte[] buf, int offset, int length) {
        super(buf, offset, length);
    }

    public byte[] getBuf() {
        return super.buf;
    }

    public void setBuf(byte[] buf) {
        super.buf = buf;
    }

    public int getPosition() {
        return super.count;
    }

    public void setPosition(int position) {
        super.count = position;
    }
}
