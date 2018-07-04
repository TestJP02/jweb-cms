/**
 * @author chi
 */
module sited.page.api {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.validation;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires java.ws.rs;
    requires sited.module;
    requires sited.service;

    exports io.sited.page.api;
    exports io.sited.page.api.category;
    exports io.sited.page.api.component;
    exports io.sited.page.api.content;
    exports io.sited.page.api.draft;
    exports io.sited.page.api.keyword;
    exports io.sited.page.api.layout;
    exports io.sited.page.api.page;
    exports io.sited.page.api.tag;
    exports io.sited.page.api.template;
    exports io.sited.page.api.variable;
    exports io.sited.page.api.comment;
    exports io.sited.page.api.statistics;
}