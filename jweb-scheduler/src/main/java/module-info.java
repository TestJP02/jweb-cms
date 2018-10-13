/**
 * @author chi
 */
module app.jweb.scheduler {
    requires transitive app.jweb.module;
    requires quartz;
    exports app.jweb.scheduler;
}