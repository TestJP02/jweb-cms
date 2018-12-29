/**
 * @author chi
 */
module app.jweb.comment.web {
    requires app.jweb.web;
    requires app.jweb.user.api;
    requires app.jweb.page.web;
    requires app.jweb.comment.api;

    exports app.jweb.comment.web;
}