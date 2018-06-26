package io.sited.undertow;

import io.sited.App;
import io.sited.ApplicationException;
import io.sited.Profile;
import io.sited.YAMLProfile;
import io.sited.resource.Resource;
import io.sited.undertow.impl.UndertowHttpContainer;
import io.sited.undertow.impl.UndertowIOHandler;
import io.undertow.Undertow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

import static io.undertow.Handlers.path;

/**
 * @author chi
 */
public class UndertowApp extends App {
    private final Logger logger = LoggerFactory.getLogger(UndertowApp.class);
    private Undertow undertow;
    private UndertowHttpContainer container;

    public UndertowApp(Path dir, Profile profile) {
        super(dir, profile);
    }

    public UndertowApp(Path dir) {
        this(dir, YAMLProfile.load(dir));
    }

    private Properties undertowOptions() {
        Properties properties = new Properties();
        InputStream inputStream = Resource.classpath("undertow.properties").openStream();
        if (inputStream == null) {
            return properties;
        }
        try (InputStream input = inputStream) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    public void start() {
        super.configure();
        container = new UndertowHttpContainer(this);
        container.getApplicationHandler().onStartup(container);

        UndertowOptions undertowOptions = options("undertow", UndertowOptions.class);
        this.undertow = Undertow.builder()
            .addHttpListener(undertowOptions.port, undertowOptions.host)
            .setServerOption(io.undertow.UndertowOptions.MAX_ENTITY_SIZE, undertowOptions.maxEntitySize)
            .setServerOption(io.undertow.UndertowOptions.MULTIPART_MAX_ENTITY_SIZE, undertowOptions.maxEntitySize)
            .setHandler(path()
                .addPrefixPath("/", new UndertowIOHandler(this, container)))
            .build();
        logger.info("server started, host={}, port={}", undertowOptions.host, undertowOptions.port);
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        this.undertow.start();
    }

    private void stop() {
        if (undertow != null) {
            undertow.stop();
        }
        if (container != null) {
            container.getApplicationHandler().onShutdown(container);
        }
    }
}
