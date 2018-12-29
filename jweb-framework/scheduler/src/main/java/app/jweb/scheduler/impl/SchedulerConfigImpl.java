package app.jweb.scheduler.impl;

import app.jweb.scheduler.SchedulerConfig;

/**
 * @author chi
 */
public class SchedulerConfigImpl implements SchedulerConfig {
    private final SchedulerService schedulerService;

    public SchedulerConfigImpl(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Override
    public SchedulerConfig schedule(String cron, Runnable task) {
        schedulerService.schedule(cron, task);
        return this;
    }
}
