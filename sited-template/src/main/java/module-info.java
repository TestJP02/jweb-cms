/**
 * @author chi
 */
module sited.template {
    requires com.google.common;
    requires java.xml.bind;
    requires slf4j.api;
    requires jericho.html;
    requires commons.jexl3;
    requires sited.module;
    exports io.sited.template;
}