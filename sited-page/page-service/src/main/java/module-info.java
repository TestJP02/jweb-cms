/**
 * @author chi
 */
module sited.page {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires java.validation;
    requires java.persistence;
    requires java.transaction;
    requires aopalliance.repackaged;
    requires javax.annotation.api;
    requires javax.inject;
    requires java.ws.rs;
    requires sited.module;
    requires sited.service;
    requires sited.page.api;
    requires sited.database;
    requires sited.message;
    requires org.commonmark;
    requires org.commonmark.ext.autolink;
    requires org.commonmark.ext.gfm.strikethrough;
    requires org.commonmark.ext.gfm.tables;
    requires org.commonmark.ext.heading.anchor;
    requires org.commonmark.ext.ins;

    exports io.sited.page;
}