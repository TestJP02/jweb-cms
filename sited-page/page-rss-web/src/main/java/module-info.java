module sited.page.rss.web{
    requires javax.inject;
    requires sited.web;
    requires java.xml.bind;
    requires sited.page.api;
    requires java.ws.rs;
    requires rome;
    requires sited.module;
    requires com.google.common;
    exports io.sited.page.rss.web;
}