/**
 * @author chi
 */
module app.jweb.comment.api {
    requires transitive app.jweb.module;
    requires app.jweb.service;

    exports app.jweb.comment.api;
    exports app.jweb.comment.api.comment;
}