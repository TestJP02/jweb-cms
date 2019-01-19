import app.jweb.lancher.Launcher;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author chi
 */
public class Main {
    public static void main(String[] args) {
//        Path path = Paths.get(System.getProperty("user.dir")).resolve("jweb-main/src/main/dist");
//        UndertowApp app = new UndertowApp(path);
//        ServiceLoader.load(AbstractModule.class).forEach(app::install);
//        app.start();

        String jwebHome = System.getProperty("jweb.home");
        Path homeDir = jwebHome == null ? Paths.get(System.getProperty("user.home")).resolve(".jweb") : Paths.get(jwebHome);
        Launcher launcher = new Launcher(homeDir);
        if (!launcher.isInstalled()) {
            launcher.install();
        } else {
            launcher.start();
        }
    }
}
