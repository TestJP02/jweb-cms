import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import io.sited.AbstractModule;
import io.sited.undertow.UndertowApp;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ServiceLoader;

/**
 * @author chi
 */
public class Main {
    static {
        System.setProperty("org.jboss.logging.provider", "slf4j");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

        PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern("%d [%thread] %-5level %logger{5} - %msg %n");
        ple.setContext(lc);
        ple.start();

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(lc);
        appender.setEncoder(ple);
        appender.start();

        root.detachAndStopAllAppenders();
        root.addAppender(appender);
        root.setLevel(Level.INFO);

        for (String packageName : Arrays.asList("org.elasticsearch", "org.mongodb", "org.apache", "org.xnio", "org.hibernate")) {
            Logger logger = (Logger) LoggerFactory.getLogger(packageName);
            logger.setLevel(Level.WARN);
        }
    }

    public static void main(String[] args) {
        Path path = Paths.get(System.getProperty("user.home")).resolve(".sited");
        UndertowApp app = new UndertowApp(path);
        ServiceLoader.load(AbstractModule.class).forEach(app::install);
        app.start();
    }
}
