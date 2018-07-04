package io.sited.page.web.service.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.sited.page.api.component.SavedComponentResponse;
import io.sited.template.AbstractComponent;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.Component;
import io.sited.template.ComponentAttribute;
import io.sited.template.Script;
import io.sited.template.StyleSheet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class SavedComponent extends AbstractComponent {
    private final Component component;
    private final SavedComponentResponse pageSavedComponent;

    public SavedComponent(Component component, SavedComponentResponse pageSavedComponent) {
        super(pageSavedComponent.name, ImmutableList.of());
        this.component = component;
        this.pageSavedComponent = pageSavedComponent;
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> attributesValues = Maps.newHashMap();
        attributesValues.putAll(attributes);
        attributesValues.putAll(pageSavedComponent.attributes);
        component.output(bindings, new Attributes(attributesValues), children, out);
    }

    @Override
    public List<Script> scripts() {
        return component.scripts();
    }

    @Override
    public List<StyleSheet> styles() {
        return component.styles();
    }

    @Override
    public Map<String, ComponentAttribute<?>> attributes() {
        return component.attributes();
    }

    public Component raw() {
        return component;
    }
}
