package app.jweb;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author chi
 */
public class AppTest {
    @Test
    public void overrides() {
        App app = new App(Paths.get("."));
        app.install(new Module1());
        app.install(new Module2());

        AbstractModule module = app.module("module1");
        assertTrue(module instanceof Module2);
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