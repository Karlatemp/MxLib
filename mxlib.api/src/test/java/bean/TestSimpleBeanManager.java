/*
 * Copyright (c) 2018-2021 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 *
 * MXLib/MXLib.mxlib-api.test/TestSimpleBeanManager.java
 *
 * Use of this source code is governed by the MIT license that can be found via the following link.
 *
 * https://github.com/Karlatemp/MxLib/blob/master/LICENSE
 */

package bean;

import io.github.karlatemp.mxlib.bean.SimpleBeanManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class TestSimpleBeanManager {
    @Test
    void testSimpleBeanManager() {
        var manager = new SimpleBeanManager();
        Assertions.assertTrue(manager.getBy(TestSimpleBeanManager.class).isEmpty());
        manager.register(TestSimpleBeanManager.class, this);
        Assertions.assertSame(manager.getBy(TestSimpleBeanManager.class).get(), this);
        var sm4 = new TestSimpleBeanManager();

        var sub = manager.newSubScope();
        Assertions.assertSame(sub.getBy(TestSimpleBeanManager.class).get(), this);
        sub.register(TestSimpleBeanManager.class, sm4);
        Assertions.assertSame(sub.getBy(TestSimpleBeanManager.class).get(), sm4);
        sub.unregister(TestSimpleBeanManager.class, null);
        Assertions.assertSame(sub.getBy(TestSimpleBeanManager.class).get(), this);


        manager.unregister(TestSimpleBeanManager.class, null);
        Assertions.assertTrue(manager.getBy(TestSimpleBeanManager.class).isEmpty());

    }
}
