package io.sited.scheduler;

import io.sited.AbstractModule;
import io.sited.Binder;
import io.sited.Configurable;
import io.sited.scheduler.impl.SchedulerConfigImpl;
import io.sited.scheduler.impl.SchedulerService;

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
