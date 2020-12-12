package format;

import io.github.karlatemp.mxlib.format.FormatArguments;
import io.github.karlatemp.mxlib.format.common.SimpleFormatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestFormatter {
    @Test
    void testSimpleFormatter() {
        var instance = SimpleFormatter.INSTANCE;
        Assertions.assertEquals(
                instance.parse("Welcome {0} to {1}")
                        .format(FormatArguments.by("Karlatemp", "MxLib"))
                        .to_string(),
                "Welcome Karlatemp to MxLib"
        );
        Assertions.assertEquals(
                instance.parse("Welcome {0} to {1} Ending")
                        .format(FormatArguments.by("Karlatemp", "MxLib"))
                        .to_string(),
                "Welcome Karlatemp to MxLib Ending"
        );
        Assertions.assertEquals(
                instance.parse("{0}{1}")
                        .format(FormatArguments.by("Karla", "temp"))
                        .to_string(),
                "Karlatemp"
        );
    }
}
