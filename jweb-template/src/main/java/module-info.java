/**
 * @author chi
 */
module jweb.template {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires jericho.html;
    requires commons.jexl3;
   requires jweb.module;
    exports app.jweb.template;
}