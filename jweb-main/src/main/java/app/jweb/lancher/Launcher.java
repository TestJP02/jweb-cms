package app.jweb.lancher;

import app.jweb.Profile;
import app.jweb.undertow.UndertowApp;

import java.nio.file.Path;

/**
 * @author chi
 */
public class Launcher extends UndertowApp {
    public Launcher(Path dir) {
        super(dir);
    }

    public Launcher(Path dir, Profile profile) {
        super(dir, profile);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }
}
