module sited.page.tracking.web {
    requires sited.web;
    requires javax.inject;
    requires sited.template;
    requires sited.page.tracking.api;
    requires sited.page.api;
    requires com.google.common;
    requires sited.module;
    exports io.sited.page.tracking.web;
}