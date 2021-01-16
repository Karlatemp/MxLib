/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-common-maven.main/MavenLoggerFactory.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package io.github.karlatemp.mxlib.maven;

import io.github.karlatemp.mxlib.MxLib;
import io.github.karlatemp.mxlib.logger.MLoggerFactory;
import org.eclipse.aether.spi.locator.Service;
import org.eclipse.aether.spi.locator.ServiceLocator;
import org.eclipse.aether.spi.log.Logger;
import org.eclipse.aether.spi.log.LoggerFactory;

public class MavenLoggerFactory implements LoggerFactory, Service {
    private MLoggerFactory factory;

    public MavenLoggerFactory() {
    }

    public MavenLoggerFactory(MLoggerFactory factory) {
        this.factory = factory;
    }

    private void initX() {
        if (factory == null) {
            synchronized (this) {
                if (factory == null) {
                    factory = MxLib.getLoggerFactory().withPrefix("maven.");
                }
            }
        }
    }

    @Override
    public Logger getLogger(String name) {
        initX();
        return new MavenLogger(factory.getLogger(name));
    }

    @Override
    public void initService(ServiceLocator locator) {
        if (factory == null) {
            factory = locator.getService(MLoggerFactory.class);
        }
    }
}
