module sited.file.web {
    requires sited.web;
    requires javax.inject;
    requires sited.file.api;
    requires com.google.common;
    requires java.xml.bind;
    requires sited.module;
    requires sited.template;
    requires java.desktop;
    requires imgscalr.lib;
    requires java.ws.rs;
    requires jersey.media.multipart;

    exports io.sited.file.web;
}