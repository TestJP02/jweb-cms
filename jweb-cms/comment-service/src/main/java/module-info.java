/**
 * @author chi
 */
module app.jweb.comment {
    requires app.jweb.service;
    requires app.jweb.comment.api;
    requires app.jweb.database;
    requires app.jweb.page.api;
    requires app.jweb.message;
    requires app.jweb.post.api;

    exports app.jweb.comment;
}