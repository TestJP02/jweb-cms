/**
 * @author chi
 */
module app.jweb.page.web {
    requires app.jweb.web;
    requires app.jweb.page.api;
    requires app.jweb.cache;
    requires app.jweb.message;

    requires org.commonmark;
    requires org.commonmark.ext.autolink;
    requires org.commonmark.ext.gfm.strikethrough;
    requires org.commonmark.ext.gfm.tables;
    requires org.commonmark.ext.heading.anchor;
    requires org.commonmark.ext.ins;

    exports app.jweb.page.web;
}