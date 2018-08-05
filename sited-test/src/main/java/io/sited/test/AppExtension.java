package io.sited.test;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.sited.AbstractModule;
import io.sited.ApplicationException;
import io.sited.database.Database;
import io.sited.log.LogbackModule;
import io.sited.test.impl.TempDirectory;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class AppExtension implements TestInstancePostProcessor, AfterTestExecutionCallback, AfterAllCallback {
    private static final String HSQL_RESET_SQL = "TRUNCATE SCHEMA public AND COMMIT";
    private final Logger logger = LoggerFactory.getLogger(AppExtension.class);

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
        Map<String, AbstractModule> all = Lists.newArrayList(ServiceLoader.load(AbstractModule.class)).stream().collect(Collectors.toMap(AbstractModule::name, module -> module));
        MockApp mockApp = new MockApp(new TempDirectory().root());
        mockApp.install(new LogbackModule());
        Arrays.asList(modules).forEach(mockApp::install);
        LinkedList<String> dependencies = Lists.newLinkedList();
        for (AbstractModule module : mockApp.modules()) {
            all.put(module.name(), module);
            dependencies.add(module.name());
        }
        Set<String> installed = Sets.newHashSet(dependencies);
        while (!dependencies.isEmpty()) {
            String name = dependencies.pollFirst();
            AbstractModule module = all.get(name);
            if (!installed.contains(name)) {
                installed.add(name);
                if (module != null) {
                    mockApp.install(module);
                }
            }
            if (module != null) {
                dependencies.addAll(module.dependencies().stream().filter(moduleName -> !installed.contains(moduleName)).collect(Collectors.toList()));
            }
        }
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
                logger.info("reset database");
                transaction.begin();
                em.createNativeQuery(HSQL_RESET_SQL).executeUpdate();
            } catch (Exception e) {
                logger.error("failed to reset database", e);
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
