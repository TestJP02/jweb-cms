package app.jweb;

import app.jweb.inject.ModuleBinder;
import app.jweb.resource.Resource;
import app.jweb.util.i18n.ResourceMessageBundle;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import org.aopalliance.intercept.MethodInterceptor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * @author chi
 */
public abstract class AbstractModule {
    private final String name;
    private final List<String> dependencies;
    private final List<Runnable> startupHooks = Lists.newArrayList();
    private final List<Runnable> shutdownHooks = Lists.newArrayList();

    private App app;
    ModuleBinder binder;

    protected AbstractModule() {
        ModuleInfo moduleInfo = moduleInfo();
        name = moduleInfo.name;
        dependencies = moduleInfo.requires;
    }

    protected AbstractModule(String name, List<String> dependencies) {
        this.name = name;
        this.dependencies = dependencies;
    }

    @SuppressWarnings("unchecked")
    protected void configure(App app) {
        if (this.app != null) {
            throw new ApplicationException("module configured, name={}", name);
        }

        this.app = app;
        this.binder = new ModuleBinder(app.binder);
        configure();
        this.binder.complete();
    }

    protected abstract void configure();

    public String name() {
        return name;
    }

    public List<String> dependencies() {
        return dependencies;
    }

    public List<String> declareRoles() {
        return Lists.newArrayList("LIST", "GET", "CREATE", "UPDATE", "DELETE", "AUDIT");
    }

    void start() {
        for (Runnable startupHook : startupHooks) {
            startupHook.run();
        }
    }

    protected void onStartup(Runnable hook) {
        startupHooks.add(hook);
    }

    void shutdown() {
        for (Runnable shutdownHook : shutdownHooks) {
            shutdownHook.run();
        }
    }

    protected void onShutdown(Runnable hook) {
        shutdownHooks.add(hook);
    }

    protected <T> T options(String name, Class<T> optionClass) {
        return app.options(name, optionClass);
    }

    protected <T> void inject(T instance) {
        app.serviceLocator().inject(instance);
    }

    public <T> T require(Class<T> type, Annotation qualifier) {
        return require((Type) type, qualifier);
    }

    public <T> T require(Type type, Annotation qualifier) {
        T service = app.serviceLocator().getService(type, qualifier);
        if (service == null) {
            throw new ApplicationException("missing binding, type={}", type);
        }
        return service;
    }

    @SuppressWarnings("unchecked")
    public <T> T require(Type type) {
        T service = app.serviceLocator().getService(type);
        if (service == null) {
            throw new ApplicationException("missing binding, type={}", type);
        }
        return service;
    }

    public <T> T require(Class<T> type) {
        return require((Type) type);
    }

    @SuppressWarnings("unchecked")
    protected <K, T extends AbstractModule & Configurable<K>> K module(Class<T> type) {
        return app.config(this, type);
    }

    protected <T> AnnotatedBindingBuilder<T> bind(Class<T> fromType) {
        return binder().bind(fromType);
    }

    protected <T> AnnotatedBindingBuilder<T> bind(Type fromType) {
        return binder().bind(fromType);
    }

    protected void bindInterceptor(Class<? extends Annotation> annotationClass, MethodInterceptor interceptor) {
        binder().bindInterceptor(annotationClass, interceptor);
    }

    protected void message(String path) {
        app.message().bind(path, new ResourceMessageBundle(path, app.resource(), app.language()));
    }

    protected Resource resource(String path) {
        return app.resource().get(path).orElseThrow(() -> new ApplicationException("missing resource, path={}", path));
    }

    protected <T> T requestInjection(T instance) {
        binder().requestInjection(instance);
        return instance;
    }

    protected App app() {
        return app;
    }

    Binder binder() {
        return checkNotNull(this.binder, "binder is not available");
    }

    private ModuleInfo moduleInfo() {
        URL moduleInfoURL = findModuleInfoURL();
        try (InputStream inputStream = moduleInfoURL.openStream()) {
            ClassReader classReader = new ClassReader(ByteStreams.toByteArray(inputStream));

            ModuleInfoClassVisitor moduleInfoClassVisitor = new ModuleInfoClassVisitor();
            classReader.accept(moduleInfoClassVisitor, ClassReader.SKIP_DEBUG);
            return moduleInfoClassVisitor.moduleInfo();
        } catch (IOException e) {
            throw new ApplicationException("could not find module-info, type={}", getClass().getCanonicalName(), e);
        }
    }

    private URL findModuleInfoURL() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            List<URL> resources = Lists.newArrayList(Iterators.forEnumeration(classLoader.getResources("module-info.class")));
            String currentClass = classLoader.getResource(getClass().getName().replaceAll("\\.", "/") + ".class").toString();
            for (URL url : resources) {
                String path = url.toString();
                path = path.substring(0, path.length() - "module-info.class".length());
                if (currentClass.startsWith(path)) {
                    return url;
                }
            }
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
        throw new ApplicationException("missing module-info.java");
    }

    static class ModuleInfo {
        public final String name;
        public final List<String> requires;

        ModuleInfo(String name, List<String> requires) {
            this.name = name;
            this.requires = requires;
        }
    }

    static class ModuleInfoClassVisitor extends ClassVisitor {
        String moduleName;
        List<String> requires = Lists.newArrayList();

        ModuleInfoClassVisitor() {
            super(Opcodes.ASM6);
        }

        @Override
        public ModuleVisitor visitModule(String name, int access, String version) {
            moduleName = name;

            return new ModuleVisitor(Opcodes.ASM6) {
                @Override
                public void visitRequire(String module, int access, String version) {
                    requires.add(module);
                }
            };
        }

        public ModuleInfo moduleInfo() {
            return new ModuleInfo(moduleName, requires);
        }
    }
}