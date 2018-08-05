package io.sited.web.impl.component;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import io.sited.template.AbstractComponent;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.Script;
import io.sited.web.impl.Theme;

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
