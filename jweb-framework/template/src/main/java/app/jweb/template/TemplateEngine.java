package app.jweb.template;

import app.jweb.resource.CompositeResourceRepository;
import app.jweb.resource.Resource;
import app.jweb.resource.ResourceRepository;
import app.jweb.template.impl.ComponentRegistry;
import app.jweb.template.impl.ElementProcessorRegistry;
import app.jweb.template.impl.TemplateComponent;
import app.jweb.template.impl.TemplateImpl;
import app.jweb.template.impl.TemplateParser;
import app.jweb.template.impl.component.ForComponent;
import app.jweb.template.impl.component.IfComponent;
import app.jweb.template.impl.component.InnerHtmlComponent;
import app.jweb.template.impl.component.InnerTextComponent;
import app.jweb.template.impl.processor.ForElementProcessorImpl;
import app.jweb.template.impl.processor.IfElementProcessorImpl;
import app.jweb.template.impl.processor.InnerHtmlElementProcessorImpl;
import app.jweb.template.impl.processor.InnerTextElementProcessorImpl;
import com.google.common.collect.Maps;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chi
 */
public class TemplateEngine {
    private final Logger logger = LoggerFactory.getLogger(TemplateEngine.class);
    private final CompositeResourceRepository resourceRepository = new CompositeResourceRepository();
    private final ComponentRegistry componentRegistry = new ComponentRegistry(this);
    private final ElementProcessorRegistry elementProcessorRegistry = new ElementProcessorRegistry();
    private final Map<String, TemplateImpl> cache = new ConcurrentHashMap<>();
    private final Map<String, Object> functions = Maps.newHashMap();
    private volatile boolean cacheEnabled = true;
    private volatile boolean skipCommentEnabled = false;

    private JexlEngine jexlEngine = new JexlBuilder().namespaces(functions).create();

    public TemplateEngine() {
        elementProcessorRegistry.add(new IfElementProcessorImpl());
        elementProcessorRegistry.add(new ForElementProcessorImpl());
        elementProcessorRegistry.add(new InnerHtmlElementProcessorImpl());
        elementProcessorRegistry.add(new InnerTextElementProcessorImpl());

        addComponent(new ForComponent());
        addComponent(new IfComponent());
        addComponent(new InnerHtmlComponent());
        addComponent(new InnerTextComponent());

        functions.put(null, new TemplateFunctions());
    }

    public TemplateEngine setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
        return this;
    }

    public TemplateEngine setSkipCommentEnabled(boolean skipCommentEnabled) {
        this.skipCommentEnabled = skipCommentEnabled;
        return this;
    }

    public TemplateEngine setAutoComponentEnabled(boolean autoComponentEnabled) {
        componentRegistry.setAutoComponentEnabled(autoComponentEnabled);
        return this;
    }

    public Optional<Template> template(String path) {
        return template(path, false);
    }

    public Optional<Template> template(String path, boolean asComponent) {
        String templateId = cacheKey(path, asComponent);
        TemplateImpl template = null;
        if (cacheEnabled) {
            template = cache.get(templateId);
        }
        if (template == null) {
            Optional<Resource> source = resourceRepository.get(path);
            if (!source.isPresent()) {
                return Optional.empty();
            }
            template = new TemplateParser(source.get(), asComponent, skipCommentEnabled, resourceRepository, componentRegistry, elementProcessorRegistry, jexlEngine).parse();
            if (cacheEnabled) {
                cache.put(templateId, template);
            }
        }
        return Optional.of(template);
    }

    public List<Component> components() {
        return componentRegistry.components();
    }

    public Optional<Component> component(String name) {
        return componentRegistry.component(name);
    }

    private String cacheKey(String path, boolean parseBody) {
        if (parseBody) {
            return path + ":body";
        }
        return path;
    }

    public TemplateEngine addElementProcessor(ElementProcessor elementProcessor) {
        elementProcessorRegistry.add(elementProcessor);
        return this;
    }

    public TemplateEngine addComponent(Component component) {
        if (component instanceof TemplateComponent) {
            ((TemplateComponent) component).setTemplateEngine(this);
        }
        componentRegistry.put(component.name(), component);
        return this;
    }

    public TemplateEngine addRepository(ResourceRepository repository) {
        resourceRepository.add(repository);
        return this;
    }

    public void addFunctions(String namespace, Object functions) {
        this.functions.put(namespace, functions);
        jexlEngine = new JexlBuilder().namespaces(this.functions).create();
    }

    public void addFunctions(Object functions) {
        addFunctions(null, functions);
    }

    public TemplateEngine reload(String path) {
        logger.info("reload template, path={}", path);
        cache.remove(cacheKey(path, false));
        cache.remove(cacheKey(path, true));
        return this;
    }
}
