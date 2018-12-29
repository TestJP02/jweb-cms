module app.jweb.file.web {
    requires app.jweb.web;
    requires app.jweb.file.api;
    requires java.desktop;
    requires imgscalr.lib;
    requires jersey.media.multipart;

    exports app.jweb.file.web;
}