module sited.page.tracking.api {
    requires sited.service;
    requires java.ws.rs;
    requires java.xml.bind;
    requires sited.module;
    exports io.sited.page.tracking.api;
    exports io.sited.page.tracking.api.tracking;
}