module app.jweb.file.admin {
    requires app.jweb.admin;
    requires app.jweb.file.api;
    requires jersey.media.multipart;

    exports app.jweb.file.admin;
}