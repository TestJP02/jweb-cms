module app.jweb.logback {
    requires transitive app.jweb.module;

    requires logback.core;
    requires logback.classic;

    exports app.jweb.log;
}