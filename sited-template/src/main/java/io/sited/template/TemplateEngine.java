package io.sited.template;

import com.google.common.collect.Maps;
import io.sited.resource.CompositeResourceRepository;
import io.sited.resource.Resource;
import io.sited.resource.ResourceRepository;
import io.sited.template.impl.ComponentRegistry;
import io.sited.template.impl.ElementProcessorRegistry;
import io.sited.template.impl.TemplateImpl;
import io.sited.template.impl.TemplateParser;
import io.sited.template.impl.component.ForComponent;
import io.sited.template.impl.component.HtmlComponent;
import io.sited.template.impl.component.IfComponent;
import io.sited.template.impl.component.TextComponent;
import io.sited.template.impl.processor.ClassElementProcessorImpl;
import io.sited.template.impl.processor.ForElementProcessorImpl;
import io.sited.template.impl.processor.HtmlElementProcessorImpl;
import io.sited.template.impl.processor.IfElementProcessorImpl;
import io.sited.template.impl.processor.TextElementProcessorImpl;
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
        elementProcessorRegistry.add(new TextElementProcessorImpl());
        elementProcessorRegistry.add(new HtmlElementProcessorImpl());
        elementProcessorRegistry.add(new ClassElementProcessorImpl());

        addComponent(new ForComponent());
        addComponent(new IfComponent());
        addComponent(new TextComponent());
        addComponent(new HtmlComponent());

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
