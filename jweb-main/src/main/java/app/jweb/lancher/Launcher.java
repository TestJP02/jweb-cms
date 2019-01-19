package app.jweb.lancher;

import app.jweb.AbstractModule;
import app.jweb.App;
import app.jweb.ApplicationException;
import app.jweb.database.DatabaseOptions;
import app.jweb.lancher.setup.SetupModule;
import app.jweb.lancher.setup.service.DataSourceFactory;
import app.jweb.lancher.setup.service.EmptyProfile;
import app.jweb.lancher.setup.web.setup.SetupRequest;
import app.jweb.undertow.UndertowApp;
import app.jweb.user.api.UserWebService;
import app.jweb.user.api.user.UpdatePasswordRequest;
import app.jweb.user.api.user.UpdateUserRequest;
import app.jweb.user.api.user.UserResponse;
import app.jweb.web.WebModule;
import com.google.common.base.Strings;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * @author chi
 */
public class Launcher {
    private final Logger logger = LoggerFactory.getLogger(Launcher.class);

    private final Path dir;
    private UndertowApp app;

    public Launcher(Path dir) {
        this.dir = dir;
    }

    public boolean isInstalled() {
        File file = dir.resolve("conf/app.yml").toFile();
        return file.isFile() && file.exists();
    }

    public boolean isMigrationNeeded() throws SQLException, LiquibaseException {
        App app = new App(dir);
        DataSource dataSource = new DataSourceFactory(app.options("database", DatabaseOptions.class)).setDir(dir).build();
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("conf/db/change-logs.yml", new ClassLoaderResourceAccessor(), database);
            return !liquibase.tagExists("0.9.0");
        }
    }


    public void migrate() throws SQLException, LiquibaseException {
        logger.info("migrating database");
        App app = new App(dir);
        DataSource dataSource = new DataSourceFactory(app.options("database", DatabaseOptions.class)).setDir(dir).build();
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("conf/db/change-logs.yml", new ClassLoaderResourceAccessor(), database);
            liquibase.update("");
            liquibase.tag("0.9.0");
        }
    }

    public void install() {
        app = new UndertowApp(dir, new EmptyProfile());
        app.install(new SetupModule(this));
        app.install(new WebModule());
        app.start();

        openInBrowser();
    }

    private void openInBrowser() {
        Runtime runtime = Runtime.getRuntime();
        String url = "http://localhost:8080";
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("win")) {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);

            } else if (os.contains("mac")) {
                runtime.exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                    "netscape", "opera", "links", "lynx"};
                StringBuilder cmd = new StringBuilder();
                for (int i = 0; i < browsers.length; i++)
                    if (i == 0)
                        cmd.append(String.format("%s \"%s\"", browsers[i], url));
                    else
                        cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
                runtime.exec(new String[]{"sh", "-c", cmd.toString()});
            }
        } catch (Exception e) {
            logger.info("failed to open browser, message={}", e.getMessage());
        }
    }

    public void restart(SetupRequest.UserView userView) {
        new Thread(() -> {
            logger.info("restart jweb...");
            try {
                Thread.sleep(2000);
                app.stop();
                start();
                updateUser(userView);
            } catch (InterruptedException e) {
                throw new ApplicationException(e);
            }
        }).start();
    }


    public void updateUser(SetupRequest.UserView userView) {
        if (Strings.isNullOrEmpty(userView.username)) {
            return;
        }
        AbstractModule module = app.module("app.jweb.user.api");
        UserWebService userWebService = module.require(UserWebService.class);
        Optional<UserResponse> userOptional = userWebService.findByUsername("admin");
        if (userOptional.isPresent()) {
            UserResponse user = userOptional.get();

            UpdateUserRequest request = new UpdateUserRequest();
            request.username = userView.username;
            request.requestBy = "SYS";
            request.userGroupIds = user.userGroupIds;
            request.description = user.description;
            request.language = user.language;
            userWebService.update(user.id, request);

            UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
            updatePasswordRequest.password = userView.password;
            updatePasswordRequest.requestBy = "SYS";
            userWebService.updatePassword(user.id, updatePasswordRequest);
        }
    }

    public void start() {
        try {
            if (isMigrationNeeded()) {
                migrate();
            }
        } catch (LiquibaseException | SQLException e) {
            throw new ApplicationException("failed to migrate database", e);
        }
        app = new UndertowApp(dir);
        ServiceLoader<AbstractModule> modules = ServiceLoader.load(AbstractModule.class);
        for (AbstractModule module : modules) {
            app.install(module);
        }

        app.start();
    }
}
