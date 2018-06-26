module sited.file.admin{
    requires javax.inject;
    requires java.ws.rs;
    requires sited.admin;
    requires sited.web;
    requires com.google.common;
    requires sited.file.api;
    requires sited.module;
    requires javax.annotation.api;
    requires jersey.media.multipart;
    requires java.xml.bind;
    exports io.sited.file.admin;
}