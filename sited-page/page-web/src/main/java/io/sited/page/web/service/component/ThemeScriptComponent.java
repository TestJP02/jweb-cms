package io.sited.page.web.service.component;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import io.sited.page.web.service.Theme;
import io.sited.page.web.service.ThemeService;
import io.sited.template.AbstractComponent;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.Script;
import io.sited.template.StringAttribute;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class ThemeScriptComponent extends AbstractComponent {
    @Inject
    ThemeService themeService;

    public ThemeScriptComponent() {
        super("theme-script", ImmutableList.of(new StringAttribute("name", null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        String themeName = attributes.get("name");
        Theme theme = themeService.theme(themeName);

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
