package io.sited.page.share.baidu.component;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.StringAttribute;
import io.sited.template.TemplateComponent;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class BaiduShareButtonsComponent extends TemplateComponent {
    public BaiduShareButtonsComponent() {
        super("share-buttons", "component/baidu-share/baidu-share.html", Lists.newArrayList(new StringAttribute("title", null)));
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream outputStream) throws IOException {
        Map<String, Object> scopeBindings = Maps.newHashMap();
        scopeBindings.putAll(bindings);
        scopeBindings.putAll(attributes);
        template().output(scopeBindings, outputStream);
    }
}
