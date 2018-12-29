package app.jweb.scheduler;

import app.jweb.AbstractModule;
import app.jweb.Binder;
import app.jweb.Configurable;
import app.jweb.scheduler.impl.SchedulerConfigImpl;
import app.jweb.scheduler.impl.SchedulerService;

/**
 * @author chi
 */
public class SchedulerModule extends AbstractModule implements Configurable<SchedulerConfig> {
    private final SchedulerService schedulerService = SchedulerService.INSTANCE;

    @Override
    protected void configure() {
        onStartup(schedulerService::start);
        onShutdown(schedulerService::stop);
    }

    @Override
    public SchedulerConfig configurator(AbstractModule module, Binder binder) {
        return new SchedulerConfigImpl(schedulerService);
    }
}
