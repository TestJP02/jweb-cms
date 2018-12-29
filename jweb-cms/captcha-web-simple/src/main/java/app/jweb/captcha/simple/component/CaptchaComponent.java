package app.jweb.captcha.simple.component;

import app.jweb.captcha.simple.CaptchaOptions;
import com.google.common.collect.ImmutableList;
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
