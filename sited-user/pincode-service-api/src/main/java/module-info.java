module sited.pincode.api {
    requires java.xml.bind;
    requires javax.inject;
    requires java.validation;
    requires java.ws.rs;
    requires sited.service;

    exports io.sited.pincode.api;
    exports io.sited.pincode.api.message;
    exports io.sited.pincode.api.pincode;
}