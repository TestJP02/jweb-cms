/**
 * @author chi
 */
module app.jweb.user {
    requires app.jweb.service;
    requires app.jweb.user.api;
    requires app.jweb.database;
    requires app.jweb.message;

    exports app.jweb.user;
}