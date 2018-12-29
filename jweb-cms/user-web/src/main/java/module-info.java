/**
 * @author chi
 */
module app.jweb.user.web {
    requires app.jweb.page.web;
    requires app.jweb.web;
    requires app.jweb.message;
    requires app.jweb.cache;
    requires app.jweb.user.api;
    requires app.jweb.captcha.web;
    requires app.jweb.pincode.api;
    requires app.jweb.pincode.web;
    requires app.jweb.page.api;
    requires scribejava.apis;
    requires scribejava.core;

    exports app.jweb.user.web;
}