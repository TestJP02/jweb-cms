module sited.file{
    requires sited.file.api;
    requires sited.database;
    requires sited.module;
    requires java.persistence;
    requires javax.inject;
    requires com.google.common;
    requires slf4j.api;
    requires java.transaction;
    requires sited.service;

    exports io.sited.file;
}