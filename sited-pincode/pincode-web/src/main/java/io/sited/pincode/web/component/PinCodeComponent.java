package io.sited.pincode.web.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.ApplicationException;
import io.sited.pincode.web.PinCodeWebOptions;
import io.sited.template.Children;
import io.sited.template.ComponentAttribute;
import io.sited.template.TemplateComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
public class PinCodeComponent extends TemplateComponent {
    @Inject
    PinCodeWebOptions options;

    public PinCodeComponent() {
        super("pincode", "component/pincode/pincode.html", Lists.newArrayList());
    }

    @Override
    public void output(Map<String, Object> bindings, Map<String, Object> attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopeBindings = Maps.newHashMap(bindings);
        scopeBindings.put("options", options);
        template().output(scopeBindings, out);
    }

    @Override
    public List<ComponentAttribute<?>> attributes() {
        return ImmutableList.of();
    }

    @Override
    public <T> ComponentAttribute<T> attribute(String s) {
        throw new ApplicationException("missing attribute");
    }
}
