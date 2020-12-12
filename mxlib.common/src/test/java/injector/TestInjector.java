package injector;

import io.github.karlatemp.mxlib.annotations.injector.AfterInject;
import io.github.karlatemp.mxlib.annotations.injector.Inject;
import io.github.karlatemp.mxlib.bean.IBeanManager;
import io.github.karlatemp.mxlib.bean.SimpleBeanManager;
import io.github.karlatemp.mxlib.common.injector.SimpleInjector;
import io.github.karlatemp.mxlib.common.injector.SimpleMethodCallerWithBeans;
import io.github.karlatemp.mxlib.common.utils.BeanManagers;
import io.github.karlatemp.mxlib.injector.IInjector;
import io.github.karlatemp.mxlib.injector.IObjectCreator;
import io.github.karlatemp.mxlib.injector.Injected;
import io.github.karlatemp.mxlib.injector.MethodCallerWithBeans;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestInjector {
    static class TestProvider {
    }

    @Test
    void test() {
        var manager = new SimpleBeanManager();
        var injector = new SimpleInjector(manager);
        var testProvider = new TestProvider();
        manager.register(IInjector.class, injector);
        manager.register(MethodCallerWithBeans.class, new SimpleMethodCallerWithBeans());
        manager.register(TestProvider.class, testProvider);

        injector.inject(StaticClassTest.class);
        Assertions.assertSame(StaticClassTest.PROVIDER, testProvider);
        Assertions.assertSame(StaticClassTest.Px.getValue(), testProvider);

        var objTest = new StaticClassTest();
        injector.inject(objTest);
        Assertions.assertSame(objTest.provider, testProvider);
        Assertions.assertSame(objTest.px.getValue(), testProvider);
    }

    public static class StaticClassTest {
        @Inject
        static TestProvider PROVIDER;
        static Injected<TestProvider> Px = new Injected.Nonnull<>(TestProvider.class);
        Injected<TestProvider> px = new Injected.Nonnull<>(TestProvider.class);

        @Inject
        TestProvider provider;

        @AfterInject
        static void afterInvoke1() {
            System.out.println("Called ai 1, " + PROVIDER);
        }

        @AfterInject
        static void afterInvoke2(@Inject IInjector injector, IInjector nil) {
            System.out.println("Called ai 2, " + PROVIDER + ", " + injector + ", " + nil);
            Assertions.assertNotNull(injector);
            Assertions.assertNull(nil);
        }

        @AfterInject
        void afterInvoke3() {
            System.out.println("Called ai 3, " + PROVIDER + ", " + provider);
        }

        @AfterInject
        void afterInvoke4(@Inject IInjector injector, IInjector nil) {
            System.out.println("Called ai 4, " + PROVIDER + ", " + provider + ", " + injector + ", " + nil);
            Assertions.assertNotNull(injector);
            Assertions.assertNull(nil);
        }
    }

    @Test
    void testAllocator() {
        var beans = BeanManagers.newStandardManager();
        class TestX {
            TestX() {
                throw new AssertionError();
            }

            @Inject
            TestX(
                    @Inject IBeanManager beanManager,
                    @Inject IInjector injector,
                    IInjector nil
            ) {
                System.out.println("Bean Manager = " + beanManager);
                System.out.println("Injector     = " + injector);
                System.out.println("nil Injector = " + nil);
                Assertions.assertNull(nil, "Nil Injector");
                Assertions.assertNotNull(beanManager, "Bean Manager");
                Assertions.assertNotNull(injector, "Injector");
            }
        }
        beans.getBy(IObjectCreator.class).get().newInstance(TestX.class);
    }
}
