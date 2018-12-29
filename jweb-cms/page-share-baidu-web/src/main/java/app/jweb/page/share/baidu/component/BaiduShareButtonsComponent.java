package app.jweb.page.share.baidu.component;


import app.jweb.page.web.AbstractPostComponent;
import app.jweb.page.web.Bindings;
import app.jweb.template.Attributes;
import app.jweb.template.Children;
import app.jweb.template.StringAttribute;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class BaiduShareButtonsComponent extends AbstractPostComponent {
    public BaiduShareButtonsComponent() {
        super("baidu-share-buttons", "component/baidu-share-buttons/baidu-share-buttons.html", Lists.newArrayList(new StringAttribute("title", null)));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream outputStream) throws IOException {
        bindings.putAll(attributes);
        template().output(bindings, outputStream);
    }
}
