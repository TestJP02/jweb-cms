import app.jweb.AbstractModule;
import app.jweb.lancher.Launcher;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ServiceLoader;

/**
 * @author chi
 */
public class Main {
    public static void main(String[] args) {
        String jwebHome = System.getProperty("jweb.home");
        Path homeDir = jwebHome == null ? Paths.get(System.getProperty("user.home")).resolve(".jweb") : Paths.get(jwebHome);
        Launcher launcher = new Launcher(homeDir);
        ServiceLoader.load(AbstractModule.class).forEach(launcher::install);
        launcher.start();
    }
}
