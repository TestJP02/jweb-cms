/**
 * @author chi
 */
module app.jweb.scheduler {
    requires com.google.common;
    requires slf4j.api;
    requires quartz;
    requires app.jweb.module;
    exports app.jweb.scheduler;
}