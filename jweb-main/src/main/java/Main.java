import app.jweb.AbstractModule;
import app.jweb.lancher.CMSApp;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ServiceLoader;

/**
 * @author chi
 */
public class Main {
    public static void main(String[] args) {
        Path path = Paths.get(System.getProperty("user.home")).resolve(".jweb");
        CMSApp app = new CMSApp(path);
        ServiceLoader.load(AbstractModule.class).forEach(app::install);
        app.start();
    }
}
