package app.jweb.lancher.derby;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author chi
 */
public class DerbyServer {
    private final Logger logger = LoggerFactory.getLogger(DerbyServer.class);

    private final Path dir;
    private final String url;

    public DerbyServer(Path dir, String url) {
        this.dir = dir;
        this.url = url;
    }

    public void start() {
        logger.info("start derby, dir={}, url={}", dir, url);
        System.setProperty("derby.system.home", dir.toString());
    }

    public void stop() {
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        logger.info("derby stopped");
    }
}
