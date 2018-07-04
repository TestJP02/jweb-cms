/**
 * @author chi
 */
module sited.scheduler {
    requires com.google.common;
    requires slf4j.api;
    requires quartz;
    requires sited.module;
    exports io.sited.scheduler;
}