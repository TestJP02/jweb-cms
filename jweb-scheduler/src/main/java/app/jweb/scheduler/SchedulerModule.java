package app.jweb.scheduler;

import app.jweb.scheduler.impl.SchedulerConfigImpl;
import app.jweb.scheduler.impl.SchedulerService;
import app.jweb.AbstractModule;
import app.jweb.Binder;
import app.jweb.Configurable;

/**
 * @author chi
 */
public class SchedulerModule extends AbstractModule implements Configurable<SchedulerConfig> {
    private final SchedulerService schedulerService = SchedulerService.INSTANCE;

    @Override
    protected void configure() {
    }

    @Override
    public SchedulerConfig configurator(AbstractModule module, Binder binder) {
        return new SchedulerConfigImpl(schedulerService);
    }

    @Override
    protected void onStartup() {
        schedulerService.start();
    }

    @Override
    protected void onShutdown() {
        schedulerService.stop();
    }
}
