package app.jweb.admin;


/**
 * @author chi
 */
public interface AdminConfig {
    <T> AdminConfig controller(Class<T> controllerClass);

    AdminConfig install(ConsoleBundle consoleBundle);

    ConsoleBundleConfig bundle(String name);
}
