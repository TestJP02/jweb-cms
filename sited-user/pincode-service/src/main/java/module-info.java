module sited.pincode {
    requires java.xml.bind;
    requires javax.inject;
    requires java.transaction;
    requires java.activation;
    requires mail;
    requires java.validation;
    requires java.ws.rs;
    requires sited.database;
    requires sited.service;
    requires sited.message;
    requires sited.pincode.api;
    requires sited.module;
    requires slf4j.api;
    requires com.google.common;
    requires java.persistence;

    exports io.sited.pincode;
}