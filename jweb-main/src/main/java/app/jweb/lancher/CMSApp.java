package app.jweb.lancher;

import app.jweb.Profile;
import app.jweb.database.DatabaseOptions;
import app.jweb.lancher.derby.DerbyServer;
import app.jweb.undertow.UndertowApp;

import java.nio.file.Path;

/**
 * @author chi
 */
public class CMSApp extends UndertowApp {
    private DerbyServer derbyServer;

    public CMSApp(Path dir) {
        super(dir);
        init();
    }

    public CMSApp(Path dir, Profile profile) {
        super(dir, profile);
        init();
    }

    private void init() {
        DatabaseOptions options = profile().options("database", DatabaseOptions.class);
        String url = options.url;
        if (isDerbyEnabled(url)) {
            derbyServer = new DerbyServer(dir().resolve("database"), url);
        }
    }

    private boolean isDerbyEnabled(String url) {
        return url != null && url.contains("derby");
    }

    @Override
    public void start() {
        if (derbyServer != null) {
            derbyServer.start();
        }
        super.start();
    }

    @Override
    public void stop() {
        super.stop();

        if (derbyServer != null) {
            derbyServer.stop();
        }
    }
}
