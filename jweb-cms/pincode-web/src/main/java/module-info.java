module app.jweb.pincode.web {
    requires javax.inject;
    requires java.ws.rs;
    requires app.jweb.web;
    requires app.jweb.template;
    requires com.google.common;
    requires app.jweb.module;
    requires java.xml.bind;
    requires app.jweb.pincode.api;

    exports app.jweb.pincode.web;
}