/**
 * @author chi
 */
module sited.page.share.addthis {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.ws.rs;
    requires sited.module;
    requires sited.web;
    requires sited.page.web;
    requires sited.template;
    exports io.sited.page.share.addthis;
}