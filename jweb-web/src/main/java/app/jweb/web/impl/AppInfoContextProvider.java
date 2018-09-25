package app.jweb.web.impl;

import app.jweb.App;
import app.jweb.web.AppInfo;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class AppInfoContextProvider implements Provider<AppInfo> {
    @Inject
    App app;

    @Override
    public AppInfo get() {
        return new AppInfoImpl(app);
    }

    static class AppInfoImpl implements AppInfo {
        private final App app;

        AppInfoImpl(App app) {
            this.app = app;
        }

        @Override
        public String name() {
            return app.name();
        }

        @Override
        public String baseURL() {
            return app.baseURL();
        }

        @Override
        public String language() {
            return app.language();
        }

        @Override
        public List<String> supportLanguages() {
            return app.supportLanguages();
        }

        @Override
        public String description() {
            return app.description();
        }

        @Override
        public String imageURL() {
            return app.imageURL();
        }

        @Override
        public Map<String, Object> properties() {
            return app.getProperties();
        }
    }
}
