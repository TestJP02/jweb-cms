package app.jweb.template.impl;

import app.jweb.template.Attribute;
import app.jweb.template.Comment;
import app.jweb.template.Component;
import app.jweb.template.ComponentAttribute;
import app.jweb.template.DocType;
import app.jweb.template.Element;
import app.jweb.template.ElementProcessor;
import app.jweb.template.Expression;
import app.jweb.template.Node;
import app.jweb.template.Script;
import app.jweb.template.StyleSheet;
import app.jweb.template.TemplateException;
import app.jweb.template.TemplateResourceException;
import app.jweb.template.Text;
import com.google.common.base.Charsets;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import app.jweb.resource.Resource;
import app.jweb.resource.ResourceRepository;
import app.jweb.template.impl.segment.AttributeExpressionSegment;
import app.jweb.template.impl.segment.ComponentSegment;
import app.jweb.template.impl.segment.CompositeSegment;
import app.jweb.template.impl.segment.ElementSegment;
import app.jweb.template.impl.segment.ExpressionSegment;
import app.jweb.template.impl.segment.IfSegment;
import app.jweb.template.impl.segment.StaticSegment;
import org.apache.commons.jexl3.JexlEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class TemplateParser {
    private static final Set<String> SELF_CLOSED_ELEMENTS = new HashSet<>(Arrays.asList("area", "base", "br", "col", "embed", "hr", "img", "input",
        "keygen", "link", "meta", "param", "source", "track", "wbr"));
    private static final Set<String> BOOL_ATTRIBUTES = new HashSet<>(Arrays.asList("checked", "selected", "disabled", "readonly", "multiple",
        "ismap", "defer", "declare", "noresize", "nowrap", "noshade", "compact", "itemscope", "async"));

    private final Resource resource;
    private final boolean parseBody;
    private final boolean parseComment;
    private final ElementProcessorRegistry elementProcessorRegistry;
    private final ComponentRegistry componentRegistry;
    private final ResourceRepository resourceRepository;
    private final List<Script> scripts = new ArrayList<>();
    private final List<StyleSheet> styles = new ArrayList<>();
    private final List<Segment> segments = new ArrayList<>();
    private final List<Component> componentRefs = Lists.newArrayList();
    private final TemplateModel templateModel;

    public TemplateParser(Resource resource, boolean parseBody, boolean parseComment, ResourceRepository resourceRepository, ComponentRegistry componentRegistry,
                          ElementProcessorRegistry elementProcessorRegistry, JexlEngine jexlEngine) {
        this.resource = resource;
        this.parseBody = parseBody;
        this.parseComment = parseComment;
        this.resourceRepository = resourceRepository;
        this.elementProcessorRegistry = elementProcessorRegistry;
        this.componentRegistry = componentRegistry;


        templateModel = new JEXLTemplateModel(jexlEngine);
    }

    @SuppressWarnings("checkstyle:NestedIfDepth")
    public TemplateImpl parse() {
        try {
            List<Node> elements = new HTMLParser(resource).parse();
            elements.forEach(this::process);

            Optional<Element> htmlOptional = html(elements);
            if (htmlOptional.isPresent()) {
                Element html = htmlOptional.get();
                head(html).ifPresent(element -> {
                    for (Node child : element.children()) {
                        parseStyle(child);
                        parseScript(child);
                    }
                });
                parseAllComponentRefs();
                head(html).ifPresent(element -> appendStyleElement(element, inlineStyles(componentRefs)));
                body(html).ifPresent(element -> appendScriptElement(element, inlineScripts(componentRefs)));

                if (parseBody) {
                    Optional<Element> body = body(html);
                    if (!body.isPresent()) {
                        throw new TemplateResourceException("missing tag <body>, path={}", resource.path());
                    }
                    body.get().children().forEach(element -> segments.add(segment(element)));
                } else {
                    segments.addAll(elements.stream().map(this::segment).collect(Collectors.toList()));
                }
            } else {
                if (parseBody) {
                    throw new TemplateResourceException("missing tag <html>, path={}", resource.path());
                } else {
                    segments.addAll(elements.stream().map(this::segment).collect(Collectors.toList()));
                }
            }
            return new TemplateImpl(resource.path(), segments, componentRefs, scripts, styles);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void process(Node node) {
        if (node instanceof Element) {
            Element element = (Element) node;
            for (ElementProcessor processor : elementProcessorRegistry.processors()) {
                processor.process(element, resource);
            }

            if (element.isDynamic()) {
                Component component = component(element);
                componentRefs.add(component);
            }

            for (int i = 0; i < element.children().size(); i++) {
                process(element.children().get(i));
            }
        }
    }

    private Component component(Element element) {
        Optional<Component> component = componentRegistry.component(element.name());
        return component.orElseThrow(() -> new TemplateException("missing component, name={}, template={}", element.name(), resource.path()));
    }

    private String inlineStyles(List<Component> components) {
        Set<String> visited = Sets.newHashSet(styles.stream().filter((Predicate<StyleSheet>) input -> input.href != null).map(styleSheet -> styleSheet.href).collect(Collectors.toList()));
        StringBuilder b = new StringBuilder();
        for (Component component : components) {
            for (StyleSheet style : component.styles()) {
                if (!Strings.isNullOrEmpty(style.innerText)) {
                    b.append(style.innerText);
                    b.append('\n');
                } else {
                    if (!visited.contains(style.href)) {
                        Optional<Resource> resource = resourceRepository.get(style.href.substring(1));
                        resource.ifPresent(r -> b.append(r.toText(Charsets.UTF_8)));
                        b.append('\n');
                        visited.add(style.href);
                    }
                }
            }
        }
        return b.toString();
    }

    private String inlineScripts(List<Component> components) {
        Set<String> visited = Sets.newHashSet(scripts.stream().filter((Predicate<Script>) input -> input.src != null).map(script -> script.src).collect(Collectors.toList()));
        StringBuilder b = new StringBuilder();
        for (Component component : components) {
            for (Script script : component.scripts()) {
                if (!Strings.isNullOrEmpty(script.innerText)) {
                    b.append(script.innerText);
                    b.append('\n');
                } else {
                    if (!visited.contains(script.src)) {
                        Optional<Resource> resource = resourceRepository.get(script.src.substring(1));
                        resource.ifPresent(resource1 -> b.append(resource1.toText(Charsets.UTF_8)));
                        b.append('\n');
                        visited.add(script.src);
                    }
                }
            }
        }
        return b.toString();
    }

    private void appendScriptElement(Element element, String innerText) {
        if (!Strings.isNullOrEmpty(innerText)) {
            Element scriptElement = new Element("script", false, element.row(), element.column(), element.source());
            Text text = new Text(innerText, element.row(), element.column(), element.source());
            scriptElement.addChild(text);
            element.addChild(scriptElement);
        }
    }

    private void appendStyleElement(Element element, String innerText) {
        if (!Strings.isNullOrEmpty(innerText)) {
            Element styleElement = new Element("style", false, element.row(), element.column(), element.source());

            Text text = new Text(innerText, element.row(), element.column(), element.source());
            styleElement.addChild(text);
            element.addChild(styleElement);
        }
    }

    private void parseScript(Node node) {
        String elementName = node.name();
        if ("script".equals(elementName)) {
            Element element = (Element) node;
            Optional<Attribute> type = element.attribute("type");
            if (type.isPresent() && !"text/javascript".equals(type.get().value())) {
                return;
            }
            Optional<Attribute> src = element.attribute("src");
            Script script = new Script();
            if (src.isPresent()) {
                script.src = src.get().value();
            } else {
                script.innerText = element.innerText();
            }
            scripts.add(script);
        }
    }

    private void parseStyle(Node node) {
        String elementName = node.name();
        if ("link".equals(elementName)) {
            Element element = (Element) node;
            Optional<Attribute> ref = element.attribute("rel");
            if (!ref.isPresent() || !"stylesheet".equalsIgnoreCase(ref.get().value())) {
                return;
            }
            Optional<Attribute> href = element.attribute("href");
            StyleSheet style = new StyleSheet();
            if (href.isPresent()) {
                style.href = href.get().value();
                styles.add(style);
            }
        } else if ("style".equals(elementName)) {
            Element element = (Element) node;
            Optional<Attribute> type = element.attribute("type");
            if (!type.isPresent() || !"stylesheet".equalsIgnoreCase(type.get().value())) {
                return;
            }
            StyleSheet style = new StyleSheet();
            style.innerText = element.innerText();
            styles.add(style);
        }
    }

    private void parseAllComponentRefs() {
        Deque<Component> stack = Lists.newLinkedList(componentRefs);
        Set<String> visited = Sets.newHashSet();
        while (!stack.isEmpty()) {
            Component current = stack.pollFirst();
            if (current != null && !visited.contains(current.name())) {
                visited.add(current.name());
                stack.addAll(current.refs());
                componentRefs.add(current);
            }
        }
    }

    @SuppressWarnings("checkstyle:NestedIfDepth")
    private List<Segment> parseText(String template, Integer row, Integer column, String source) {
        List<Segment> segments = new ArrayList<>();

        StringBuilder b = new StringBuilder();
        char[] text = template.toCharArray();

        int state = 0;
        for (int i = 0; i < text.length; i++) {
            char c = text[i];
            if (c == '\n') {
                b.append(c);
            } else if (c == '{' && state == 0) {
                if (i - 1 > 0 && text[i - 1] == '\\') {
                    b.append(c);
                } else if (i + 1 < text.length && text[i + 1] == '{') {
                    state = 1;
                    i++;

                    if (b.length() > 0) {
                        Segment segment = new StaticSegment(b.toString());
                        b.delete(0, b.length());
                        segments.add(segment);
                    }
                } else {
                    b.append(c);
                }
            } else if (c == '}' && state == 1) {
                if (i - 1 > 0 && text[i - 1] == '\\') {
                    b.append(c);
                } else if (i + 1 < text.length && text[i + 1] == '}') {
                    state = 0;
                    i++;

                    if (b.length() > 0) {
                        String content = b.toString();

                        Expression textExpression = templateModel.add(content, null, row, column, source);
                        Segment segment = new ExpressionSegment(textExpression);
                        b.delete(0, b.length());
                        segments.add(segment);
                    }
                } else {
                    b.append(c);
                }
            } else {
                b.append(c);
            }
        }

        if (b.length() > 0) {
            segments.add(new StaticSegment(b.toString()));
        }
        return segments;
    }

    private Optional<Element> html(List<Node> nodes) {
        for (Node node : nodes) {
            if ("html".equalsIgnoreCase(node.name())) {
                return Optional.of((Element) node);
            }
        }
        return Optional.empty();
    }

    private Optional<Element> head(Element html) {
        for (Node child : html.children()) {
            if ("head".equalsIgnoreCase(child.name())) {
                return Optional.of((Element) child);
            }
        }
        return Optional.empty();
    }

    private Optional<Element> body(Element html) {
        for (Node child : html.children()) {
            if ("body".equalsIgnoreCase(child.name())) {
                return Optional.of((Element) child);
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("checkstyle:NestedIfDepth")
    private Segment segment(Node node) {
        if (node.isText()) {
            CompositeSegment composite = new CompositeSegment();
            for (Segment segment : parseText(((Text) node).innerText(), node.row(), node.column(), node.source())) {
                composite.addChild(segment);
            }
            return composite;
        }

        if (node.isComment()) {
            Comment comment = (Comment) node;
            if (parseComment) {
                return new StaticSegment(comment.outerHtml());
            } else if (isMSComment(comment)) {
                return new StaticSegment(comment.outerHtml());
            } else {
                return new StaticSegment("");
            }
        }

        if (node.isDocType()) {
            return new StaticSegment(((DocType) node).outerHtml());
        }

        Element element = (Element) node;
        if (element.isDynamic()) {
            String componentName = element.name();
            Map<String, Expression> attributeExpressions = new HashMap<>();
            element.attributes().forEach(attribute -> {
                if (attribute.isDynamic()) {
                    attributeExpressions.put(attribute.name(), parseExpression(element, attribute.value(), attribute.defaultValue(), element.source()));
                } else {
                    attributeExpressions.put(attribute.name(), new ConstantExpression(attribute.value()));
                }
            });
            Component component = componentRegistry.component(componentName).orElseThrow(() -> new TemplateException("missing component, name={}, template={}", element.name(), resource.path()));
            for (ComponentAttribute<?> attribute : component.attributes().values()) {
                if (!attributeExpressions.containsKey(attribute.name())) {
                    attributeExpressions.put(attribute.name(), new ConstantExpression(attribute.defaultValue()));
                }
            }
            List<Segment> children = Lists.newArrayList();
            for (Node child : element.children()) {
                children.add(segment(child));
            }
            return new ComponentSegment(component, children.isEmpty() ? new EmptyChildren() : new ChildrenImpl(children), attributeExpressions);
        } else {
            List<Segment> start = new ArrayList<>();
            List<Segment> end = new ArrayList<>();
            start.add(new StaticSegment("<" + element.name()));
            element.attributes().forEach(attribute -> {
                if (isBoolAttribute(attribute.name())) {
                    if (attribute.isDynamic()) {
                        IfSegment attributeIfSegment = new IfSegment(parseExpression(element, attribute.value(), attribute.defaultValue(), attribute.source()));
                        attributeIfSegment.addChild(new StaticSegment(' ' + attribute.name()));
                        start.add(attributeIfSegment);
                    } else {
                        start.add(new StaticSegment(' ' + attribute.name()));
                    }
                } else {
                    if (attribute.isDynamic()) {
                        start.add(new StaticSegment(' ' + attribute.name() + "=\""));
                        if (!Strings.isNullOrEmpty(attribute.value())) {
                            start.add(new AttributeExpressionSegment(parseExpression(element, attribute.value(), attribute.defaultValue(), attribute.source())));
                        }
                        start.add(new StaticSegment("\""));
                    } else {
                        start.add(new StaticSegment(' ' + attribute.name() + "=\"" + attribute.value() + "\""));
                    }
                }
            });

            if (isSelfClosedElement(element.name())) {
                start.add(new StaticSegment("/>"));
            } else {
                start.add(new StaticSegment(">"));
                end.add(new StaticSegment("</" + element.name() + ">"));
            }

            ElementSegment segment = new ElementSegment(start, end);
            for (Node child : element.children()) {
                segment.addChild(segment(child));
            }
            return segment;
        }
    }

    private boolean isMSComment(Comment comment) {
        String html = comment.outerHtml();
        return html.startsWith("<!--[if") || html.startsWith("<!--<![");
    }

    private Expression parseExpression(Element element, String content, Object defaultValue, String source) {
        if (defaultValue == null) {
            return templateModel.add(content, null, element.row(), element.column(), source);
        } else if (isExpression(defaultValue)) {
            String expr = (String) defaultValue;
            Expression defaultExpr = templateModel.add(expr.substring(2, expr.length() - 2), null, element.row(), element.column(), source);
            return templateModel.add(content, defaultExpr, element.row(), element.column(), source);
        } else {
            return templateModel.add(content, new ConstantExpression(defaultValue), element.row(), element.column(), source);
        }
    }

    private boolean isExpression(Object value) {
        if (value instanceof String) {
            String expr = (String) value;
            return expr.startsWith("{{") && expr.endsWith("}}");
        }
        return false;
    }

    private boolean isSelfClosedElement(String tagName) {
        return SELF_CLOSED_ELEMENTS.contains(tagName.toLowerCase(Locale.ENGLISH));
    }

    private boolean isBoolAttribute(String attributeName) {
        return BOOL_ATTRIBUTES.contains(attributeName.toLowerCase(Locale.ENGLISH));
    }
}
