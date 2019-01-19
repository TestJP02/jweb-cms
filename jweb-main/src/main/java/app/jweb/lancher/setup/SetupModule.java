package app.jweb.lancher.setup;

import app.jweb.lancher.Launcher;
import app.jweb.lancher.setup.service.Console;
import app.jweb.lancher.setup.service.SetupManager;
import app.jweb.lancher.setup.web.HealthCheckController;
import app.jweb.lancher.setup.web.SetupPageController;
import app.jweb.lancher.setup.web.SetupWebController;
import app.jweb.web.AbstractWebModule;
import com.google.common.collect.Lists;


/**
 * @author chi
 */
public class SetupModule extends AbstractWebModule {
    private final Launcher launcher;

    public SetupModule(Launcher launcher) {
        super("app.jweb.setup", Lists.newArrayList("app.jweb.web", "app.jweb.user.api"));
        this.launcher = launcher;
    }

    @Override
    protected void configure() {
        bind(Launcher.class).toInstance(launcher);
        bind(SetupManager.class);
        Console console = new Console();
        bind(Console.class).toInstance(console);
        message("conf/messages/setup");

        web().controller(SetupWebController.class);
        web().controller(SetupPageController.class);
        web().controller(HealthCheckController.class);
    }
}
