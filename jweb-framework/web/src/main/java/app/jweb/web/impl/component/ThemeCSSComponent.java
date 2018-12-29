package app.jweb.web.impl.component;

import app.jweb.template.AbstractComponent;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.StyleSheet;
import app.jweb.web.impl.Theme;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class ThemeCSSComponent extends AbstractComponent {
    private final Theme theme;

    public ThemeCSSComponent(Theme theme) {
        super("theme-css", ImmutableList.of());
        this.theme = theme;
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
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
