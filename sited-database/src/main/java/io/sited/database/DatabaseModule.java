package io.sited.database;

import com.google.common.collect.ImmutableList;
import io.sited.AbstractModule;
import io.sited.ApplicationException;
import io.sited.Binder;
import io.sited.Configurable;
import io.sited.Environment;
import io.sited.database.impl.DatabaseConfigImpl;
import io.sited.database.impl.DatabaseImpl;
import io.sited.database.impl.TransactionalInterceptor;

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
    }

    @Override
    protected void onStartup() {
        if (database != null) {
            database.start();
        }
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
