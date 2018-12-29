/**
 * @author chi
 */
module app.jweb.template {
    requires transitive app.jweb.module;
    requires jericho.html;
    requires commons.jexl3;
    exports app.jweb.template;
}