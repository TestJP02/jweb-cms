package app.jweb.database;


import app.jweb.ApplicationException;
import com.google.common.base.Strings;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
public class DataSourceFactory {
    private static final Pattern JDBC_URL = Pattern.compile("jdbc:(.+?):.*");
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
        dataSource.setUrl(url(options.url, dir));
        dataSource.setDriverClassName(driver(options.driver, options.url));
        dataSource.setUsername(options.username);
        dataSource.setPassword(options.password);
        dataSource.setMaxTotal(options.pool.max);
        dataSource.setMinIdle(options.pool.min);
        return dataSource;
    }

    private String url(String url, Path dir) {
        String prefix = "jdbc:hsqldb:file:";
        if (url.startsWith(prefix) && dir != null) {
            int p = url.indexOf(';');
            if (p < 0) {
                p = url.length();
            }
            String file = url.substring(prefix.length(), p);
            if (file.startsWith("./")) {
                return prefix + dir.resolve(file).normalize().toString() + url.substring(p);
            }
        }
        return url;
    }

    private String driver(String driver, String url) {
        if (!Strings.isNullOrEmpty(driver)) {
            return driver;
        }
        Matcher matcher = JDBC_URL.matcher(url);
        if (!matcher.matches()) {
            throw new ApplicationException("invalid database url, url={}", url);
        }
        String vendor = matcher.group(1);
        switch (vendor) {
            case "mysql":
                return "com.mysql.cj.jdbc.Driver";
            case "oracle":
                return "oracle.jdbc.driver.OracleDriver";
            case "postgresql":
                return "org.postgresql.Driver";
            case "sqlserver":
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            case "hsqldb":
                return "org.hsqldb.jdbc.JDBCDriver";
            default:
                throw new ApplicationException("unknown driver, vendor={}, url={}", vendor, url);
        }
    }
}
