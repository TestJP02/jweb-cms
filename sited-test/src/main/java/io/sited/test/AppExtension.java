package io.sited.test;


import io.sited.AbstractModule;
import io.sited.ApplicationException;
import io.sited.database.Database;
import io.sited.test.impl.TempDirectory;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Arrays;

/**
 * @author chi
 */
public class AppExtension implements TestInstancePostProcessor, AfterTestExecutionCallback, AfterAllCallback {
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(AppExtension.class, MockApp.class));
        Install install = testInstance.getClass().getDeclaredAnnotation(Install.class);
        if (install == null) {
            throw new ApplicationException("missing @Install annotation");
        }
        MockApp app = (MockApp) store.getOrComputeIfAbsent(MockApp.class, type -> createApp(install.value()));
        app.inject(testInstance);
    }

    private MockApp createApp(Class<? extends AbstractModule>... modules) {
        MockApp mockApp = new MockApp(new TempDirectory().root());
        Arrays.asList(modules).forEach(mockApp::install);
        mockApp.start();
        return mockApp;
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(AppExtension.class, MockApp.class));
        MockApp app = (MockApp) store.get(MockApp.class);
        if (app.isInstalled("sited.database")) {
            AbstractModule module = app.module("sited.database");
            Database database = module.require(Database.class);
            EntityManager em = database.em();
            EntityTransaction transaction = em.getTransaction();
            try {
                transaction.begin();
                em.createNativeQuery("TRUNCATE SCHEMA public AND COMMIT").executeUpdate();
            } finally {
                transaction.commit();
                em.close();
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        ExtensionContext.Store store = context.getStore(ExtensionContext.Namespace.create(AppExtension.class, MockApp.class));
        MockApp app = (MockApp) store.get(MockApp.class);
        if (app != null) {
            app.stop();
            new TempDirectory(app.dir()).delete();
        }
    }
}
