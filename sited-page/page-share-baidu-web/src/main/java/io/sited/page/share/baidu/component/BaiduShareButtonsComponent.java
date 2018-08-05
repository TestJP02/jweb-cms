package io.sited.page.share.baidu.component;


import com.google.common.collect.Lists;
import io.sited.page.web.AbstractPageComponent;
import io.sited.page.web.Bindings;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.StringAttribute;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author chi
 */
public class BaiduShareButtonsComponent extends AbstractPageComponent {
    public BaiduShareButtonsComponent() {
        super("baidu-share-buttons", "component/baidu-share-buttons/baidu-share-buttons.html", Lists.newArrayList(new StringAttribute("title", null)));
    }

    @Override
    public void output(Bindings bindings, Attributes attributes, Children children, OutputStream outputStream) throws IOException {
        bindings.putAll(attributes);
        template().output(bindings, outputStream);
    }
}
