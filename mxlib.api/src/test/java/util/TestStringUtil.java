package util;

import io.github.karlatemp.mxlib.utils.StringUtils.BkColors;
import org.junit.jupiter.api.Test;

public class TestStringUtil {
    @Test
    void testBkTrans() {
        System.out.println(BkColors.replaceToBkMessage(BkColors._1 + "Hello " + BkColors._B + "World"));
    }
}
