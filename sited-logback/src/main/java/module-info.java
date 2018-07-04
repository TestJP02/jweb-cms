module sited.logback {
    requires java.xml.bind;
    requires javax.inject;
    requires java.validation;
    requires java.ws.rs;
    requires sited.module;
    requires javax.annotation.api;
    requires logback.core;
    requires logback.classic;
    requires slf4j.api;

    exports io.sited.log;
}