import liquibase.CatalogAndSchema;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.serializer.core.yaml.YamlChangeLogSerializer;
import liquibase.structure.core.Column;
import liquibase.structure.core.Data;
import liquibase.structure.core.ForeignKey;
import liquibase.structure.core.Index;
import liquibase.structure.core.PrimaryKey;
import liquibase.structure.core.Sequence;
import liquibase.structure.core.Table;
import liquibase.structure.core.UniqueConstraint;
import liquibase.structure.core.View;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws LiquibaseException, SQLException, IOException, ParserConfigurationException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/main?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("root");

        java.sql.Connection connection = dataSource.getConnection();
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
        Liquibase liquibase = new Liquibase("db-changes.yml", new ClassLoaderResourceAccessor(), database);

        CatalogAndSchema catalogAndSchema = new CatalogAndSchema(null, "main");
        DiffToChangeLog changeLog = new DiffToChangeLog(new DiffOutputControl(false, false, true, null));

        liquibase.generateChangeLog(catalogAndSchema, changeLog, new PrintStream(new FileOutputStream("./change-logs.yml")), new YamlChangeLogSerializer(), snapTypes());
    }

    private static Class[] snapTypes() {
        return new Class[]{UniqueConstraint.class, Sequence.class, Table.class, View.class, ForeignKey.class, PrimaryKey.class, Index.class, Column.class, Data.class};
    }
}
