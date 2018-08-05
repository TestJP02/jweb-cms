package io.sited.pincode.web.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.pincode.web.PinCodeWebOptions;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.web.AbstractWebComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class PinCodeComponent extends AbstractWebComponent {
    @Inject
    PinCodeWebOptions options;

    public PinCodeComponent() {
        super("pincode", "component/pincode/pincode.html", Lists.newArrayList());
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Map<String, Object> scopeBindings = Maps.newHashMap(bindings);
        scopeBindings.put("options", options);
        template().output(scopeBindings, out);
    }
}
