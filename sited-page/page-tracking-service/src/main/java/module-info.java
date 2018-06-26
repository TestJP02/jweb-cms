module sited.page.tracking {
    requires sited.page.tracking.api;
    requires sited.database;
    requires sited.message;
    requires sited.page.api;
    requires java.persistence;
    requires javax.inject;
    requires java.transaction;
    requires com.google.common;
    requires sited.module;
    requires sited.service;
    exports io.sited.page.tracking;
}