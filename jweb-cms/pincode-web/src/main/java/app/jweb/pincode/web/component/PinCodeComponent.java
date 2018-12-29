package app.jweb.pincode.web.component;

import app.jweb.pincode.web.PinCodeWebOptions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.web.AbstractWebComponent;

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
