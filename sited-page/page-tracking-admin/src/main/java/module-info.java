module sited.page.tracking.admin{
    requires sited.admin;
    requires java.xml.bind;
    requires javax.inject;
    requires javax.annotation.api;
    requires java.ws.rs;
    requires sited.page.tracking.api;
    requires sited.page.api;
    requires sited.module;
    exports io.sited.page.tracking.admin;
}