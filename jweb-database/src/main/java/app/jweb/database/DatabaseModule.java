package app.jweb.database;

import app.jweb.AbstractModule;
import app.jweb.ApplicationException;
import app.jweb.Binder;
import app.jweb.Configurable;
import app.jweb.Environment;
import app.jweb.database.impl.DatabaseConfigImpl;
import app.jweb.database.impl.DatabaseImpl;
import app.jweb.database.impl.TransactionalInterceptor;
import com.google.common.collect.ImmutableList;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author chi
 */
public class DatabaseModule extends AbstractModule implements Configurable<DatabaseConfig> {
    private DatabaseImpl database;

    @Override
    protected void configure() {
        DatabaseOptions options = options("database", DatabaseOptions.class);
        if (Boolean.TRUE.equals(options.createTableEnabled) && app().env() != Environment.DEV) {
            throw new ApplicationException("PROD env doesn't allow to auto create tables");
        }
        this.database = new DatabaseImpl(options, app().dir());
        bind(Database.class).toInstance(this.database);
        bindInterceptor(Transactional.class, new TransactionalInterceptor(this.database));

        onStartup(database::start);
    }

    @Override
    public List<String> declareRoles() {
        return ImmutableList.of();
    }

    @Override
    public DatabaseConfig configurator(AbstractModule module, Binder binder) {
        return new DatabaseConfigImpl(binder, database);
    }
}
