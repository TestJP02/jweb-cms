package io.sited.page.web.service.component;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import io.sited.page.web.service.Theme;
import io.sited.page.web.service.ThemeService;
import io.sited.template.AbstractComponent;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.StringAttribute;
import io.sited.template.StyleSheet;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class ThemeCSSComponent extends AbstractComponent {
    @Inject
    ThemeService themeService;

    public ThemeCSSComponent() {
        super("theme-css", ImmutableList.of(new StringAttribute("name", null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        String themeName = attributes.get("name");
        Theme theme = themeService.theme(themeName);

        for (StyleSheet styleSheet : theme.styles()) {
            if (styleSheet.innerText != null) {
                out.write("<style>".getBytes(Charsets.UTF_8));
                out.write(styleSheet.innerText.getBytes(Charsets.UTF_8));
                out.write("</style>".getBytes(Charsets.UTF_8));
            } else {
                out.write("<link rel=\"stylesheet\" href=\"".getBytes(Charsets.UTF_8));
                out.write(styleSheet.href.getBytes(Charsets.UTF_8));
                out.write("\">".getBytes(Charsets.UTF_8));
            }
        }
    }
}
