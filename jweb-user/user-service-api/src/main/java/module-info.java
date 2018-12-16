/**
 * @author chi
 */
module app.jweb.user.api {
    requires app.jweb.service;

    exports app.jweb.user.api;
    exports app.jweb.user.api.group;
    exports app.jweb.user.api.token;
    exports app.jweb.user.api.user;
    exports app.jweb.user.api.oauth;
}