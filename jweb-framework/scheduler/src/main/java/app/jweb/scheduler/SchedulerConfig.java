package app.jweb.scheduler;

/**
 * @author chi
 */
public interface SchedulerConfig {
    SchedulerConfig schedule(String cron, Runnable task);
}
