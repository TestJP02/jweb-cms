/**
 * @author chi
 */
module jweb.scheduler {
    requires com.google.common;
    requires slf4j.api;
    requires quartz;
   requires jweb.module;
    exports app.jweb.scheduler;
}