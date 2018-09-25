/**
 * @author chi
 */
module jweb.cache {
    requires com.google.common;
    requires java.xml.bind;
    requires javax.annotation.api;
    requires javax.inject;
    requires slf4j.api;
    requires java.validation;
    requires java.ws.rs;
   requires jweb.module;

    exports app.jweb.cache;
}