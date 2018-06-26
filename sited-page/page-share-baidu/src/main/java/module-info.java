/**
 * @author chi
 */
module sited.page.share.baidu {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
    requires sited.module;
    requires sited.web;
    requires sited.template;

    exports io.sited.page.share.baidu;
}