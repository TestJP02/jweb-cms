module sited.file.api {
    requires java.xml.bind;
    requires javax.inject;
    requires java.validation;
    requires java.ws.rs;
    requires sited.service;
    requires sited.module;
    requires javax.annotation.api;

    exports io.sited.file.api;
    exports io.sited.file.api.file;
    exports io.sited.file.api.directory;
}