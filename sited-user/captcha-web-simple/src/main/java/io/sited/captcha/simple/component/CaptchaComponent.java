package io.sited.captcha.simple.component;

import com.google.common.collect.ImmutableList;
import io.sited.captcha.simple.CaptchaOptions;
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
public class CaptchaComponent extends AbstractWebComponent {
    @Inject
    CaptchaOptions options;

    public CaptchaComponent() {
        super("captcha", "component/captcha/captcha.html", ImmutableList.of());
    }

    @Override
    public String name() {
        return "captcha";
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        attributes.forEach(bindings::put);
        bindings.put("captchaEnabled", options.enabled);
        template().output(bindings, out);
    }
}
