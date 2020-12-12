package util;

import io.github.karlatemp.mxlib.common.utils.BeanManagers;
import io.github.karlatemp.mxlib.common.utils.SimpleJarScanner;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

public class TestJarScanner {
    @Test
    void test() throws Throwable {
        var scanner = new SimpleJarScanner(BeanManagers.newStandardManager());
        scanner.scan(new File("."), new ArrayList<>()).forEach(System.out::println);
    }


}
