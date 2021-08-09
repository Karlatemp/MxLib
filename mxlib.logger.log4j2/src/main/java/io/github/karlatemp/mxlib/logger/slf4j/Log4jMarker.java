/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-logger-log4j2.main/Log4jMarker.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.logger.slf4j;

import io.github.karlatemp.mxlib.logger.MMarket;
import org.apache.logging.log4j.Marker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class Log4jMarker implements MMarket {
    static Log4jMarker of(@Nullable Marker marker) {
        if (marker == null) return null;
        return new Log4jMarker(marker);
    }

    final Marker marker;

    Log4jMarker(Marker marker) {
        this.marker = marker;
    }

    @Override
    public @NotNull String getName() {
        return marker.getName();
    }
}
