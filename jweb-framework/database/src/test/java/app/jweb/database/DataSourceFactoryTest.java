package app.jweb.database;

import app.jweb.test.TempDirectory;
import app.jweb.test.TempDirectoryExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author chi
 */
@ExtendWith(TempDirectoryExtension.class)
class DataSourceFactoryTest {
    @Test
    void build() throws SQLException {
        DatabaseOptions options = new DatabaseOptions();
        options.url = "jdbc:hsqldb:mem:db;sql.syntax_mys=true";
        options.createTableEnabled = true;
        DataSource dataSource = new DataSourceFactory(options).build();

        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
        }
    }

    @Test
    void buildHSQLFile(@TempDirectory Path dir) throws SQLException {
        DatabaseOptions options = new DatabaseOptions();
        options.url = "jdbc:hsqldb:file:./db;sql.syntax_mys=true";
        options.createTableEnabled = true;
        DataSource dataSource = new DataSourceFactory(options).setDir(dir).build();

        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
        }
    }
}