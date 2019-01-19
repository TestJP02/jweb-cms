package app.jweb.lancher.setup.service;

import app.jweb.ApplicationException;
import app.jweb.YAMLProfile;
import app.jweb.database.DatabaseOptions;
import app.jweb.lancher.setup.web.setup.SetupRequest;
import app.jweb.resource.StringResource;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author chi
 */
public class SetupManager {
    public void createProfile(Path path, SetupRequest request) {
        YAMLProfile profile = new YAMLProfile(new StringResource("conf/app.yml", "app:\n  name: "));
        profile.setOptions("app", appOptions(request));
        profile.setOptions("user", userOptions(request.user));
        profile.setOptions("database", databaseOptions(request.database));

        try {
            Files.createDirectories(path.getParent());
            Files.write(path, profile.toYAML().getBytes(Charsets.UTF_8));
        } catch (IOException e) {
            throw new ApplicationException("failed to create conf/app.yml", e);
        }
    }


    public void validateDatabase(SetupRequest.DatabaseView database) {
        if ("mysql".equals(database.vendor)) {
            DatabaseOptions mySQLOptions = new DatabaseOptions();
            mySQLOptions.url = databaseURL(database);
            mySQLOptions.username = database.username;
            mySQLOptions.password = database.password;
            DataSource dataSource = new DataSourceFactory(mySQLOptions).build();
            try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("select 1")) {
                statement.execute();
            } catch (SQLException e) {
                throw new ApplicationException(e);
            }
        }
    }

    private Map<String, Object> appOptions(SetupRequest setupRequest) {
        Map<String, Object> userOptions = Maps.newHashMap();
        userOptions.put("name", setupRequest.name);
        userOptions.put("language", setupRequest.language);
        return userOptions;
    }

    private Map<String, Object> userOptions(SetupRequest.UserView userView) {
        Map<String, Object> userOptions = Maps.newHashMap();
        userOptions.put("username", userView.username);
        userOptions.put("password", userView.password);
        userOptions.put("email", userView.email);
        return userOptions;
    }

    private Map<String, Object> databaseOptions(SetupRequest.DatabaseView databaseView) {
        Map<String, Object> databaseOptions = Maps.newHashMap();
        if ("mysql".equals(databaseView.vendor)) {
            databaseOptions.put("url", databaseURL(databaseView));
            databaseOptions.put("username", databaseView.username);
            databaseOptions.put("password", databaseView.password);
        } else {
            databaseOptions.put("url", "jdbc:h2:./main");
        }
        return databaseOptions;
    }

    private String databaseURL(SetupRequest.DatabaseView database) {
        return "jdbc:" +
            database.vendor +
            "://" +
            database.host +
            ":" +
            database.port +
            "/" +
            database.database +
            "?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false";
    }
}
