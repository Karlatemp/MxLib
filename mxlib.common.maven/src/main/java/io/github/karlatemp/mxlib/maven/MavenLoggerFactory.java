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
