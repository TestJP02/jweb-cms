package app.jweb.web;

import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public interface AppInfo {
    String name();

    String baseURL();

    String language();

    List<String> supportLanguages();

    String description();

    String imageURL();

    Map<String, Object> properties();
}
