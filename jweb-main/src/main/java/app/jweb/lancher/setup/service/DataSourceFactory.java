package app.jweb.lancher.setup.service;


import app.jweb.database.DatabaseOptions;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.nio.file.Path;

/**
 * @author chi
 */
public class DataSourceFactory {
    private final DatabaseOptions options;
    private Path dir;

    public DataSourceFactory(DatabaseOptions options) {
        this.options = options;
    }

    public DataSourceFactory setDir(Path dir) {
        this.dir = dir;
        return this;
    }

    public DataSource build() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(resetBaseDir(options.url));
        dataSource.setUsername(options.username);
        dataSource.setPassword(options.password);
        dataSource.setMaxTotal(options.pool.max);
        dataSource.setMinIdle(options.pool.min);
        return dataSource;
    }

    private String resetBaseDir(String jdbcURL) {
        if (jdbcURL != null) {
            return jdbcURL.replace(":./", ':' + dir.resolve("database").toString() + '/');
        }
        return null;
    }
}
