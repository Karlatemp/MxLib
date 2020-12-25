package util;

import io.github.karlatemp.mxlib.utils.Streams;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TestStreams {
    @Test
    void testStream() {
        Streams.iterate(Arrays.asList("1", "2", "487", "87waex").iterator())
                .forEach(System.out::println);
    }
}
