package app.jweb.page.tracking.baidu.service.component;

import app.jweb.page.tracking.baidu.service.BaiduTrackingScriptService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
public class BaiduTrackingComponent extends AbstractWebComponent {
    @Inject
    BaiduTrackingScriptService baiduTrackingScriptService;

    public BaiduTrackingComponent() {
        super("baidu-tracking", "component/baidu-tracking/baidu-tracking.html", Lists.newArrayList());
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream outputStream) throws IOException {
        if (baiduTrackingScriptService.isEnabled()) {
            Map<String, Object> scopedBindings = Maps.newHashMap();
            scopedBindings.putAll(bindings);
            scopedBindings.put("script", baiduTrackingScriptService.script());
            template().output(scopedBindings, outputStream);
        }
    }
}
