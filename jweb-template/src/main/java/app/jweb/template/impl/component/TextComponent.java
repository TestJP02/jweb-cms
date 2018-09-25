package app.jweb.template.impl.component;

import app.jweb.template.AbstractComponent;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.ObjectAttribute;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.html.HtmlEscapers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class TextComponent extends AbstractComponent {
    public TextComponent() {
        super("text", Lists.newArrayList(new ObjectAttribute<>("content", Object.class, null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        Object content = attributes.get("content");
        if (content == null) {
            children.output(bindings, out);
        } else {
            out.write(HtmlEscapers.htmlEscaper().escape(content.toString()).getBytes(Charsets.UTF_8));
        }
    }
}
