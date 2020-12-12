package util;

import io.github.karlatemp.mxlib.common.utils.PathUtils;
import io.github.karlatemp.mxlib.common.utils.SimpleClassLocator;
import io.github.karlatemp.mxlib.reflect.Reflections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;

public class TestClassLocator {
    @Test
    public void run() {
        var locator = new SimpleClassLocator();
        System.out.println(locator.context());
        System.out.println(locator.findLocate(TestClassLocator.class));
        System.out.println(locator.findLocate("util.TestClassLocator"));
        System.out.println(locator.findLocate(String.class));
        System.out.println(locator.findLocate("java.lang.String"));
        System.out.println(locator.findLocate(ClassReader.class));
        System.out.println(locator.findLocate("org.objectweb.asm.ClassReader"));
    }

    @Test
    public void testIsJre() {
        var locator = new SimpleClassLocator();
        Assertions.assertTrue(PathUtils.isJre(
                locator.findLocate("java.lang.String")
        ));
        Assertions.assertTrue(PathUtils.isJre(
                locator.findLocate(String.class)
        ));
        Assertions.assertFalse(PathUtils.isJre(
                locator.findLocate("util.TestClassLocator")
        ));
        Assertions.assertFalse(PathUtils.isJre(
                locator.findLocate(TestClassLocator.class)
        ));
        Assertions.assertFalse(PathUtils.isJre(
                locator.findLocate("org.objectweb.asm.ClassReader")
        ));
        Assertions.assertFalse(PathUtils.isJre(
                locator.findLocate(ClassReader.class)
        ));
    }
}
