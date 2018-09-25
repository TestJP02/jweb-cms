package app.jweb.web.impl.component;

import app.jweb.template.AbstractComponent;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.Script;
import app.jweb.web.impl.Theme;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class ThemeScriptComponent extends AbstractComponent {
    private final Theme theme;

    public ThemeScriptComponent(Theme theme) {
        super("theme-script", ImmutableList.of());
        this.theme = theme;
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        for (Script script : theme.scripts()) {
            if (script.innerText != null) {
                out.write("<script>".getBytes(Charsets.UTF_8));
                out.write(script.innerText.getBytes(Charsets.UTF_8));
                out.write("</script>".getBytes(Charsets.UTF_8));
            } else {
                out.write("<script src=\">".getBytes(Charsets.UTF_8));
                out.write(script.src.getBytes(Charsets.UTF_8));
                out.write("\"></script>".getBytes(Charsets.UTF_8));
            }
        }
    }
}
