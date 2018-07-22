package io.sited.database.impl;


import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import io.sited.ApplicationException;
import io.sited.database.DataSourceFactory;
import io.sited.database.Database;
import io.sited.database.DatabaseOptions;
import io.sited.database.Query;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
public class DatabaseImpl implements Database {
    private static final Pattern JDBC_URL = Pattern.compile("jdbc:(.+?):.*");
    private final ThreadLocal<Transaction> currentTransaction = new ThreadLocal<>();
    private final Set<String> entityClassNames = Sets.newHashSet();
    private final DatabaseOptions options;
    private final Path dir;
    private EntityManagerFactory emf;

    public DatabaseImpl(DatabaseOptions options, Path dir) {
        this.options = options;
        this.dir = dir;
    }

    public void start() {
        emf = createEMF();
    }

    public void addEntityClassName(Class<?> entityClass) {
        entityClassNames.add(entityClass.getName());
    }

    private EntityManagerFactory createEMF() {
        PersistenceUnitInfo persistenceUnitInfo = persistenceUnitInfo();
        Map<String, Object> configuration = new HashMap<>();
        return new EntityManagerFactoryBuilderImpl(new PersistenceUnitInfoDescriptor(persistenceUnitInfo), configuration).build();
    }

    private PersistenceUnitInfoImpl persistenceUnitInfo() {
        return new PersistenceUnitInfoImpl("database", ImmutableList.copyOf(entityClassNames), properties());
    }

    Transaction transaction() {
        Transaction transaction = currentTransaction.get();
        if (transaction == null) {
            transaction = new Transaction(this);
            currentTransaction.set(transaction);
        }
        return transaction;
    }

    void resetTransaction() {
        currentTransaction.remove();
    }

    private Properties properties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", dialect(options.dialect, options.url));
        if (options.createTableEnabled) {
            properties.put("hibernate.hbm2ddl.auto", "create-drop");
        }
        properties.put("hibernate.connection.datasource", new DataSourceFactory(options).setDir(dir).build());
        properties.put("javax.persistence.validation.mode", "NONE");
        properties.put("hibernate.show_sql", options.showSQLEnabled);
        return properties;
    }

    private String dialect(String dialect, String url) {
        if (!Strings.isNullOrEmpty(dialect)) {
            return dialect;
        }
        Matcher matcher = JDBC_URL.matcher(url);
        if (!matcher.matches()) {
            throw new ApplicationException("invalid database url, url={}", url);
        }
        String vendor = matcher.group(1);
        switch (vendor) {
            case "mysql":
                return "org.hibernate.dialect.MySQL55Dialect";
            case "oracle":
                return "org.hibernate.dialect.Oracle10gDialect";
            case "postgresql":
                return "org.hibernate.dialect.PostgreSQL91Dialect";
            case "sqlserver":
                return "org.hibernate.dialect.SQLServer2012Dialect";
            case "hsqldb":
                return "org.hibernate.dialect.HSQLDialect";
            default:
                throw new ApplicationException("unknown dialect, vendor={}, url={}", vendor, url);
        }
    }


    @Override
    public <T> Query<T> query(String sql, Class<T> viewClass, Object... params) {
        return new RepositoryImpl<>(viewClass, this).query(sql, params);
    }

    @Override
    public int execute(String sql, Object... params) {
        EntityManager em = em();
        try {
            javax.persistence.Query query = em.createQuery(sql);
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
            return query.executeUpdate();
        } catch (Exception e) {
            throw new ApplicationException("failed to execute sql, sql={}, params={}", sql, params, e);
        } finally {
            em.close();
        }
    }

    @Override
    public EntityManager em() {
        Transaction transaction = currentTransaction.get();
        if (transaction == null) {
            return emf.createEntityManager();
        } else {
            return transaction.em();
        }
    }


    static class PersistenceUnitInfoImpl implements PersistenceUnitInfo {
        private final String name;
        private final List<String> entityClassNames;
        private final Properties properties;

        PersistenceUnitInfoImpl(String name, List<String> entityClassNames, Properties properties) {
            this.name = name;
            this.entityClassNames = entityClassNames;
            this.properties = properties;
        }

        @Override
        public String getPersistenceUnitName() {
            return name;
        }

        @Override
        public String getPersistenceProviderClassName() {
            return null;
        }

        @Override
        public PersistenceUnitTransactionType getTransactionType() {
            return PersistenceUnitTransactionType.RESOURCE_LOCAL;
        }

        @Override
        public DataSource getJtaDataSource() {
            return null;
        }

        @Override
        public DataSource getNonJtaDataSource() {
            return null;
        }

        @Override
        public List<String> getMappingFileNames() {
            return null;
        }

        @Override
        public List<URL> getJarFileUrls() {
            return null;
        }

        @Override
        public URL getPersistenceUnitRootUrl() {
            return null;
        }

        @Override
        public List<String> getManagedClassNames() {
            return entityClassNames;
        }

        @Override
        public boolean excludeUnlistedClasses() {
            return false;
        }

        @Override
        public SharedCacheMode getSharedCacheMode() {
            return null;
        }

        @Override
        public ValidationMode getValidationMode() {
            return ValidationMode.AUTO;
        }

        @Override
        public Properties getProperties() {
            return properties;
        }

        @Override
        public String getPersistenceXMLSchemaVersion() {
            return null;
        }

        @Override
        public ClassLoader getClassLoader() {
            return null;
        }

        @Override
        public void addTransformer(ClassTransformer transformer) {
        }

        @Override
        public ClassLoader getNewTempClassLoader() {
            return null;
        }
    }
}
