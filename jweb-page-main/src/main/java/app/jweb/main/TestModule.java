package app.jweb.main;

import app.jweb.main.web.TestController;
import app.jweb.web.AbstractWebModule;
import com.google.common.collect.Lists;

/**
 * @author chi
 */
public class TestModule extends AbstractWebModule {
    public TestModule() {
        super("test", Lists.newArrayList("app.jweb.web", "app.jweb.user.api", "app.jweb.page.api", "app.jweb.file.api"));
    }

    @Override
    protected void configure() {
        web().controller(TestController.class);
    }
}
