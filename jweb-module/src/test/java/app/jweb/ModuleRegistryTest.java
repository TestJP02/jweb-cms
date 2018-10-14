package app.jweb;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author chi
 */
class ModuleRegistryTest {
    @Test
    void dependencies() {
        ModuleRegistry registry = new ModuleRegistry();
        registry.install(new TestModule1());
        registry.install(new TestModule2());
        registry.install(new TestModule3());
        registry.install(new TestModule4());

        List<String> dependencies = registry.recursiveDependencies("test2");
        assertEquals(2, dependencies.size());
    }

    @Test
    void exposed() {
        ModuleRegistry registry = new ModuleRegistry();
        registry.install(new TestModule1());
        registry.install(new TestModule2());
        registry.install(new TestModule3());
        registry.install(new TestModule4());

        registry.validate();

        ArrayList<AbstractModule> modules = Lists.newArrayList(registry);
        assertEquals(3, modules.size());
    }

    @Test
    void validate() {
        Assertions.assertThrows(ApplicationException.class, () -> {
            ModuleRegistry registry = new ModuleRegistry();
            registry.install(new TestModule5());
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

    static class TestModule5 extends TestModule1 {
        protected TestModule5() {
            super("test5", Lists.newArrayList("test5"));
        }

        @Override
        protected void configure() {

        }
    }
}