module sited.pincode.web {
    requires javax.inject;
    requires java.ws.rs;
    requires sited.web;
    requires sited.template;
    requires com.google.common;
    requires sited.module;
    requires java.xml.bind;
    requires sited.pincode.api;

    exports io.sited.pincode.web;
}