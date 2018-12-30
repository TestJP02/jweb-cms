package app.jweb;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author chi
 */
public class AppTest {
    @Test
    void dependencies() {
        App app = new App(Paths.get("."));
        app.install(new TestModule1());
        app.install(new TestModule2());
        app.install(new TestModule3());
        app.install(new TestModule4());

        List<String> dependencies = app.recursiveDependencies("test2");
        assertEquals(1, dependencies.size());
    }

    @Test
    void install() {
        App registry = new App(Paths.get("."));
        registry.install(new TestModule1());
        registry.install(new TestModule2());
        registry.install(new TestModule3());
        registry.install(new TestModule4());

        registry.validate();
    }

    @Test
    void validate() {
        Assertions.assertThrows(ApplicationException.class, () -> {
            App registry = new App(Paths.get("."));
            registry.install(new TestModule5());
            registry.install(new TestModule6());
            registry.validate();
        });
    }

    static class TestModule1 extends AbstractModule {
        protected TestModule1(String name, List<String> dependencies) {
            super(name, dependencies);
        }

        protected TestModule1() {
            super("test1", Lists.newArrayList());
        }

        @Override
        protected void configure() {

        }
    }

    static class TestModule2 extends AbstractModule {
        protected TestModule2() {
            super("test2", Lists.newArrayList("test1"));
        }

        @Override
        protected void configure() {

        }
    }

    static class TestModule3 extends AbstractModule {
        protected TestModule3() {
            super("test3", Lists.newArrayList());
        }

        @Override
        protected void configure() {

        }
    }


    static class TestModule4 extends TestModule1 {
        protected TestModule4() {
            super("test4", Lists.newArrayList("test3"));
        }

        @Override
        protected void configure() {

        }
    }

    static class TestModule5 extends AbstractModule {
        protected TestModule5() {
            super("test5", Lists.newArrayList("test6"));
        }

        @Override
        protected void configure() {

        }
    }

    static class TestModule6 extends AbstractModule {
        protected TestModule6() {
            super("test6", Lists.newArrayList("test5"));
        }

        @Override
        protected void configure() {

        }
    }

    static class Module1 extends AbstractModule {
        Module1() {
            super("module1", Lists.newArrayList());
        }

        Module1(String name, List<String> dependencies) {
            super(name, dependencies);
        }

        @Override
        protected void configure() {
        }
    }

    static class Module2 extends Module1 {
        Module2() {
            super("module2", Lists.newArrayList());
        }

        @Override
        protected void configure() {
        }
    }
}