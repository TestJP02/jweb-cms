package io.sited.test;

import com.google.common.collect.Lists;
import io.sited.AbstractModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(AppExtension.class)
@Install(MockAppTest.TestModule.class)
class MockAppTest {
    @Inject
    MockApp app;

    @Test
    void configure() {
        assertNotNull(app);
    }

    public static class TestModule extends AbstractModule {
        public TestModule() {
            super("test", Lists.newArrayList());
        }

        @Override
        protected void configure() {
        }
    }
}