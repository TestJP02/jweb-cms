package app.jweb;

import app.jweb.inject.ModuleBinder;
import app.jweb.resource.ClasspathResourceRepository;
import app.jweb.resource.CompositeResourceRepository;
import app.jweb.resource.FileResourceRepository;
import app.jweb.resource.ResourceRepository;
import app.jweb.util.JSON;
import app.jweb.util.exception.Exceptions;
import app.jweb.util.i18n.CompositeMessageBundle;
import app.jweb.util.i18n.MessageBundle;
import app.jweb.util.type.Types;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.utilities.general.internal.MessageInterpolatorImpl;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.glassfish.jersey.server.validation.ValidationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.ws.rs.core.Application;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author chi
 */
public class App extends Application {
    final Logger logger = LoggerFactory.getLogger(App.class);
    private final ModuleRegistry moduleRegistry = new ModuleRegistry();
    private final List<Object> singletons = Lists.newArrayList();
    private final Map<String, Object> resourceSingletons = Maps.newHashMap();
    private final List<Class<?>> classes = Lists.newArrayList();
    private final Map<String, Class<?>> resourceClasses = Maps.newHashMap();
    private final Map<String, Object> properties = Maps.newHashMap();

    final Path dir;
    final AppOptions options;
    final Profile profile;
    final ResourceRepository repository;
    final CompositeMessageBundle compositeMessageBundle;

    ServiceLocator serviceLocator;
    ModuleBinder binder;

    public App(Path dir, Profile profile) {
        this.dir = dir;
        this.profile = profile;
        repository = new CompositeResourceRepository(new FileResourceRepository(dir), new ClasspathResourceRepository(""));
        options = profile.options("app", AppOptions.class);
        if (options.language == null) {
            options.language = Locale.getDefault().toLanguageTag();
        } else {
            Locale.setDefault(Locale.forLanguageTag(options.language));
        }
        if (options.supportLanguages == null || options.supportLanguages.isEmpty()) {
            options.supportLanguages = Lists.newArrayList(options.language);
        }
        if (!options.baseURL.endsWith("/")) {
            options.baseURL = options.baseURL + "/";
        }
        compositeMessageBundle = new CompositeMessageBundle();
    }

    public App(Path dir) {
        this(dir, YAMLProfile.load(dir));
    }

    public Path dir() {
        return dir;
    }

    public String name() {
        return options.name;
    }

    public String baseURL() {
        return options.baseURL;
    }

    public String language() {
        return options.language;
    }

    public List<String> supportLanguages() {
        return options.supportLanguages;
    }

    public <T> T options(String name, Class<T> optionClass) {
        return profile.options(name, optionClass);
    }

    public Environment env() {
        return options.env == null ? Environment.PROD : options.env;
    }

    public List<AbstractModule> modules() {
        return ImmutableList.copyOf(moduleRegistry);
    }

    public ServiceLocator serviceLocator() {
        if (serviceLocator == null) {
            throw new ApplicationException("service locator is not available during configure phase");
        }
        return serviceLocator;
    }

    public ResourceRepository resource() {
        return repository;
    }

    public CompositeMessageBundle message() {
        return compositeMessageBundle;
    }

    @Override
    public Set<Object> getSingletons() {
        ImmutableSet.Builder<Object> builder = ImmutableSet.builder();
        builder.addAll(singletons);
        builder.addAll(resourceSingletons.values());
        return builder.build();
    }

    @Override
    public Set<Class<?>> getClasses() {
        ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
        builder.addAll(classes);
        builder.addAll(resourceClasses.values());
        return builder.build();
    }

    @Override
    public Map<String, Object> getProperties() {
        return ImmutableMap.copyOf(properties);
    }

    public void inject(Object instance) {
        serviceLocator().inject(instance);
    }

    public final void register(Object singleton) {
        checkNotNull(singleton, "resource can't null");
        if (isResource(singleton.getClass())) {
            javax.ws.rs.Path path = singleton.getClass().getDeclaredAnnotation(javax.ws.rs.Path.class);
            if (resourceSingletons.containsKey(path.value())) {
                logger.info("override resource, path={}, original={}, resource={}", path.value(), resourceSingletons.get(path.value()), singleton.getClass());
            }
            resourceSingletons.put(path.value(), singleton);
        } else {
            singletons.add(singleton);
        }
    }

    public final void register(Class<?> type) {
        checkNotNull(type, "resource can't null");
        if (isResource(type)) {
            javax.ws.rs.Path path = type.getDeclaredAnnotation(javax.ws.rs.Path.class);
            if (resourceClasses.containsKey(path.value())) {
                logger.info("override resource, path={}, original={}, resource={}", path.value(), resourceClasses.get(path.value()), type);
            }
            resourceClasses.put(path.value(), type);
        } else {
            classes.add(type);
        }
    }

    private boolean isResource(Class<?> resourceClass) {
        return resourceClass.isAnnotationPresent(javax.ws.rs.Path.class);
    }

    public final void property(String name, Object value) {
        properties.put(name, value);
    }

    public void install(AbstractModule module) {
        moduleRegistry.install(module);
    }

    public void install(Class<? extends AbstractModule> moduleClass) {
        try {
            AbstractModule module = moduleClass.getDeclaredConstructor().newInstance();
            install(module);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ApplicationException("failed to create module, moduleClass={}", moduleClass.getCanonicalName(), e);
        }
    }

    public boolean isInstalled(String name) {
        return moduleRegistry.isInstalled(name);
    }

    @SuppressWarnings("unchecked")
    public AbstractModule module(String moduleName) {
        return moduleRegistry.module(moduleName);
    }

    protected void configure() {
        logger.info("init app, name={}, language={}, dir={}", name(), language(), dir());
        binder = new ModuleBinder();

        binder.bind(App.class).toInstance(this);
        binder.bind(MessageBundle.class).toInstance(message());

        binder.bind(Configuration.class).toInstance(Validation.byDefaultProvider().configure()
            .messageInterpolator(new MessageInterpolatorImpl())
            .addProperty("hibernate.validator.fail_fast", "true"));
        register(ValidationFeature.class);

        JacksonJaxbJsonProvider jacksonProvider = new JacksonJaxbJsonProvider();
        jacksonProvider.setMapper(JSON.OBJECT_MAPPER);
        register(jacksonProvider);
        register(JacksonFeature.class);

        register(binder.raw());
        register(new DefaultContainerLifecycleListener(this));

        register(new AppEventListener());
        register(DefaultExceptionMapper.class);

        moduleRegistry.createGraph();
        moduleRegistry.forEach(this::configure);
    }

    private void configure(AbstractModule module) {
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        try {
            module.configure(this);
            logger.info("install module [{}], type={}, in {}ms", module.name(), module.getClass().getCanonicalName(), stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Throwable e) {
            throw new ApplicationException("install module [{}] failed, type={}", module.name(), module.getClass().getCanonicalName(), e);
        }
    }

    final void onStartup() {
        Stopwatch w = Stopwatch.createStarted();
        StringBuilder b = new StringBuilder(256);
        b.append('\t');
        resourceClasses.forEach((path, resourceClass) -> {
            b.append(path).append(" => ").append(resourceClass.getCanonicalName()).append("\n\t");
        });
        resourceSingletons.forEach((path, resource) -> {
            b.append(path).append(" => ").append(resource.getClass().getCanonicalName()).append("\n\t");
        });
        logger.info("{} root resources:\n{}", resourceClasses.size() + resourceSingletons.size(), b.toString());
        moduleRegistry.forEach(this::start);
        logger.info("app started, in {}ms", w.elapsed(TimeUnit.MILLISECONDS));
    }

    private void start(AbstractModule module) {
        try {
            for (Object instance : module.binder.injectionRequests()) {
                module.inject(instance);
            }
            module.binder = null;
            module.start();
        } catch (Throwable e) {
            throw new ApplicationException("failed to start module, name={}, type={}", module.name(), module.getClass().getCanonicalName(), e);
        }
    }

    final void onShutdown() {
        moduleRegistry.forEach(AbstractModule::shutdown);
        logger.info("app stopped");
    }

    @SuppressWarnings("unchecked")
    public <K, T extends AbstractModule & Configurable<K>> K config(AbstractModule module, Class<T> type) {
        List<String> candidates = Lists.newArrayList(moduleRegistry.dependencies(module.name()));
        candidates.add(module.name());
        for (String dependency : candidates) {
            AbstractModule m = module(dependency);
            Type[] interfaces = m.getClass().getGenericInterfaces();
            for (Type interfaceClass : interfaces) {
                if (Types.isGeneric(interfaceClass)) {
                    ParameterizedType parameterizedType = (ParameterizedType) interfaceClass;
                    if (Types.rawClass(parameterizedType).equals(Configurable.class) && m.getClass().equals(type)) {
                        Configurable<K> configurable = (T) module(dependency);
                        return configurable.configurator(module, module.binder());
                    }
                }
            }
        }
        throw new ApplicationException("missing dependency, module={}, require={}", module.getClass(), type);
    }

    public boolean isAPIEnabled() {
        return options.apiEnabled;
    }

    public boolean isWebEnabled() {
        return options.webEnabled;
    }

    public String description() {
        return options.description;
    }

    public String imageURL() {
        return options.imageURL;
    }

    public String host() {
        return options.host;
    }

    public String port() {
        return options.port;
    }

    public class AppEventListener implements ApplicationEventListener {
        RequestEventListener requestEventListener = event -> {
            if (event.getType() == RequestEvent.Type.ON_EXCEPTION) {
                ContainerRequest request = event.getContainerRequest();
                logger.error("request failed, method={}, path={}\n{}", request.getMethod(), request.getAbsolutePath(), Exceptions.stackTrace(event.getException()));
            }
        };

        @Override
        public void onEvent(ApplicationEvent event) {
        }

        @Override
        public RequestEventListener onRequest(RequestEvent requestEvent) {
            return requestEventListener;
        }
    }

}
