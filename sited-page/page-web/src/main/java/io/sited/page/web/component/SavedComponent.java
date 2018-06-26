package io.sited.page.web.component;

import com.google.common.collect.ImmutableList;
import io.sited.page.api.component.SavedComponentResponse;
import io.sited.template.AbstractComponent;
import io.sited.template.Children;
import io.sited.template.Component;
import io.sited.template.Script;
import io.sited.template.StyleSheet;
import io.sited.template.TemplateEngine;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author chi
 */
public class SavedComponent extends AbstractComponent {
    private final TemplateEngine templateEngine;
    private final SavedComponentResponse pageSavedComponent;

    public SavedComponent(SavedComponentResponse pageSavedComponent, TemplateEngine templateEngine) {
        super(pageSavedComponent.name, ImmutableList.of());
        this.pageSavedComponent = pageSavedComponent;
        this.templateEngine = templateEngine;
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Optional<Component> component = templateEngine.component(pageSavedComponent.componentName);
        if (component.isPresent()) {
            component.get().output(bindings, pageSavedComponent.attributes, children, out);
        }
    }

    @Override
    public List<Script> scripts() {
        Optional<Component> component = templateEngine.component(pageSavedComponent.componentName);
        return component.map(Component::scripts).orElseGet(ImmutableList::of);
    }

    @Override
    public List<StyleSheet> styles() {
        Optional<Component> component = templateEngine.component(pageSavedComponent.componentName);
        return component.map(Component::styles).orElseGet(ImmutableList::of);
    }
}
