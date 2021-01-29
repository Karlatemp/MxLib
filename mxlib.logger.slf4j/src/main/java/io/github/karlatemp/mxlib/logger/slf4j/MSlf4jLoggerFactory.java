/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger-slf4j.main/MSlf4jLoggerFactory.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.slf4j;

import io.github.karlatemp.mxlib.logger.MLoggerFactory;
import io.github.karlatemp.mxlib.logger.MMarket;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.Marker;

public class MSlf4jLoggerFactory implements ILoggerFactory {
    private final MLoggerFactory delegate;

    public MSlf4jLoggerFactory(MLoggerFactory delegate) {
        this.delegate = delegate;
    }

    @Override
    public Logger getLogger(String name) {
        return new MSlf4JLogger(delegate.getLogger(name), this);
    }

    public MMarket toMMarket(Marker marker) {
        if (marker == null) return null;
        if (marker instanceof MMarket) return (MMarket) marker;
        //noinspection NullableProblems
        return marker::getName;
    }
}
