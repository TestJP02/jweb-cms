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
import com.google.common.base.Splitter;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
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
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author chi
 */
public class App extends Application {
    private final Logger logger = LoggerFactory.getLogger(App.class);
    private final Map<String, ModuleNode> roots = Maps.newHashMap();
    private List<String> orderedModules;

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

    public List<AbstractModule> modules() {
        List<String> moduleNames = orderedModules();
        List<AbstractModule> modules = Lists.newArrayList();
        for (String moduleName : moduleNames) {
            ModuleNode moduleNode = moduleNode(moduleName).orElseThrow();
            if (!moduleNode.isOverrided()) {
                modules.add(moduleNode.module);
            }
        }
        return modules;
    }

    public void install(AbstractModule module) {
        if (module == null) {
            throw new ApplicationException("module can't be null");
        }
        List<String> names = Splitter.on('.').splitToList(module.name());
        Map<String, ModuleNode> nodes = roots;
        ModuleNode parent = null;
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            ModuleNode current = nodes.get(name);
            if (current == null) {
                current = new ModuleNode();
                current.nodeName = name;
                current.parent = parent;
                current.status = ModuleStatus.INSTALLED;
                nodes.put(name, current);
            }

            if (i == names.size() - 1) {
                if (current.module != null) {
                    throw new ApplicationException("duplicate module, name={}, module={}, exist={}", module.name(),
                        module.getClass().getCanonicalName(), current.module.getClass().getCanonicalName());
                }
                current.module = module;
            } else {
                nodes = current.children;
                parent = current;
            }
        }
    }

    public void install(Class<? extends AbstractModule> moduleClass) {
        try {
            AbstractModule module = moduleClass.getDeclaredConstructor().newInstance();
            install(module);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ApplicationException("failed to create module, moduleClass={}", moduleClass.getCanonicalName(), e);
        }
    }

    public boolean isInstalled(String moduleName) {
        Optional<ModuleNode> moduleNode = moduleNode(moduleName);
        return moduleNode.isPresent() && moduleNode.get().module != null;
    }

    @SuppressWarnings("unchecked")
    public AbstractModule module(String moduleName) {
        Optional<ModuleNode> moduleNodeOptional = moduleNode(moduleName);
        if (moduleNodeOptional.isEmpty()) {
            throw new ApplicationException("missing module, name={}", moduleName);
        }
        ModuleNode moduleNode = moduleNodeOptional.get();
        if (moduleNode.isOverrided()) {
            return moduleNode.children.values().iterator().next().module;
        }
        return moduleNode.module;
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

        validate();

        for (String moduleName : orderedModules()) {
            ModuleNode moduleNode = moduleNode(moduleName).orElseThrow();
            if (moduleNode.isOverrided()) {
                moduleNode.status = ModuleStatus.CONFIGURED;
            } else {
                try {
                    Stopwatch w = Stopwatch.createStarted();
                    configure(moduleNode.module);
                    moduleNode.status = ModuleStatus.CONFIGURED;
                } catch (Exception e) {
                    throw new ApplicationException("failed to install module {}, type={}", moduleName, moduleNode.module.getClass().getCanonicalName(), e);
                }
            }
        }
    }

    public void validate() {
        Map<String, Set<String>> dependencies = Maps.newHashMap();
        Set<String> moduleNames = Sets.newHashSet(orderedModules());
        for (String moduleName : moduleNames) {
            if (!dependencies.containsKey(moduleName)) {
                dependencies.put(moduleName, Sets.newHashSet());
            }
            for (String dependency : dependencies(moduleName)) {
                dependencies.get(moduleName).add(dependency);

                if (dependencies.containsKey(dependency)) {
                    dependencies.get(moduleName).addAll(dependencies.get(dependency));
                }
            }
            if (dependencies.get(moduleName).contains(moduleName)) {
                throw new ApplicationException("cycle module dependency, name={}", moduleName);
            }
        }
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

    private List<String> orderedModules() {
        if (orderedModules == null) {
            Graph<String> graph = createGraph();
            Deque<String> readyModules = Lists.newLinkedList();
            for (String node : graph.nodes()) {
                if (graph.predecessors(node).isEmpty()) {
                    readyModules.push(node);
                }
            }
            Set<String> visited = Sets.newLinkedHashSet();
            while (!readyModules.isEmpty()) {
                String moduleName = readyModules.pollFirst();
                visited.add(moduleName);

                Set<String> successors = graph.successors(moduleName);
                for (String successor : successors) {
                    ModuleNode moduleNode = moduleNode(successor).orElseThrow();
                    boolean ready = true;
                    for (String dependency : moduleNode.module.dependencies()) {
                        if (isInstalled(dependency) && !visited.contains(dependency)) {
                            ready = false;
                            break;
                        }
                    }
                    if (ready) {
                        visited.add(successor);
                    }
                }
            }
            orderedModules = ImmutableList.copyOf(visited);
        }
        return orderedModules;
    }


    public Graph<String> createGraph() {
        MutableGraph<String> graph = GraphBuilder.directed().allowsSelfLoops(false)
            .build();
        for (ModuleNode module : installedNodes()) {
            graph.addNode(module.module.name());
            for (String dependency : module.module.dependencies()) {
                if (isInstalled(dependency)) {
                    graph.addNode(dependency);
                    graph.putEdge(dependency, module.module.name());
                }
            }
            for (ModuleNode child : module.children.values()) {
                graph.addNode(child.module.name());
                graph.putEdge(child.module.name(), module.module.name());
            }
        }
        return ImmutableGraph.copyOf(graph);
    }

    private List<ModuleNode> installedNodes() {
        Deque<ModuleNode> stack = Lists.newLinkedList();
        List<ModuleNode> nodes = Lists.newArrayList();

        stack.addAll(roots.values());

        while (!stack.isEmpty()) {
            ModuleNode node = stack.pollFirst();
            if (node == null) {
                break;
            }
            if (node.module != null) {
                nodes.add(node);
            }
            if (!node.children.isEmpty()) {
                stack.addAll(node.children.values());
            }
        }
        return nodes;
    }

    private Optional<ModuleNode> moduleNode(String moduleName) {
        List<String> names = Splitter.on('.').splitToList(moduleName);
        Map<String, ModuleNode> nodes = roots;
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            ModuleNode current = nodes.get(name);
            if (current == null) {
                break;
            }
            if (i == names.size() - 1) {
                return Optional.of(current);
            } else {
                nodes = current.children;
            }
        }
        return Optional.empty();
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
        for (String moduleName : orderedModules()) {
            ModuleNode moduleNode = moduleNode(moduleName).orElseThrow();
            if (!moduleNode.isOverrided()) {
                start(moduleNode.module);
            }
            moduleNode.status = ModuleStatus.STARTED;
        }
        logger.info("app started, in {}ms", w.elapsed(TimeUnit.MILLISECONDS));
    }

    private void start(AbstractModule module) {
        try {
            module.onStartup();
            for (Object instance : module.binder.injectionRequests()) {
                module.inject(instance);
            }
            module.binder = null;
        } catch (Throwable e) {
            throw new ApplicationException("failed to start module, name={}, type={}", module.name(), module.getClass().getCanonicalName(), e);
        }
    }

    final void onShutdown() {
        for (String moduleName : orderedModules()) {
            ModuleNode moduleNode = moduleNode(moduleName).orElseThrow();
            if (!moduleNode.isOverrided()) {
                shutdown(moduleNode.module);
            }
            moduleNode.status = ModuleStatus.STOPPED;
        }
        logger.info("app stopped");
    }

    private void shutdown(AbstractModule module) {
        try {
            module.shutdown();
        } catch (Throwable e) {
            throw new ApplicationException("failed to start module, name={}, type={}", module.name(), module.getClass().getCanonicalName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public <K, T extends AbstractModule & Configurable<K>> K config(AbstractModule module, Class<T> type) {
        List<String> candidates = Lists.newArrayList(recursiveDependencies(module.name()));
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


    public List<String> recursiveDependencies(String moduleName) {
        Deque<String> deque = Lists.newLinkedList();
        deque.add(moduleName);
        Set<String> visited = Sets.newHashSet();
        while (!deque.isEmpty()) {
            String current = deque.pollFirst();
            if (!Objects.equals(current, moduleName)) {
                visited.add(current);
            }
            List<String> dependencies = dependencies(current);
            for (String dependency : dependencies) {
                if (!visited.contains(dependency)) {
                    deque.add(dependency);
                }
            }
        }
        return Lists.newArrayList(visited);
    }

    private List<String> dependencies(String moduleName) {
        ModuleNode module = moduleNode(moduleName).orElseThrow();
        return module.module.dependencies().stream().filter(name -> !Objects.equals(name, moduleName) && isInstalled(name)).collect(Collectors.toList());
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

    public enum ModuleStatus {
        INSTALLED, CONFIGURED, STARTED, STOPPED
    }

    public static class ModuleNode {
        public String nodeName;
        public ModuleNode parent;
        public Map<String, ModuleNode> children = Maps.newHashMap();
        public AbstractModule module;
        public ModuleStatus status;

        public boolean isOverrided() {
            return module != null && !children.isEmpty();
        }
    }
}
