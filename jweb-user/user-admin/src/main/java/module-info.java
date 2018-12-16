/**
 * @author chi
 */
module app.jweb.user.admin {
    requires app.jweb.admin;
    requires app.jweb.user.api;
    requires app.jweb.captcha.web;
    requires app.jweb.message;
    exports app.jweb.user.admin;
}