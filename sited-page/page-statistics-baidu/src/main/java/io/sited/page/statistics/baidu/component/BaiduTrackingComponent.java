package io.sited.page.statistics.baidu.component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.sited.page.statistics.baidu.service.BaiduStatisticsScriptService;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.template.TemplateComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class BaiduTrackingComponent extends TemplateComponent {
    @Inject
    BaiduStatisticsScriptService baiduStatisticsScriptService;

    public BaiduTrackingComponent() {
        super("baidu-tracking", "component/baidu-tracking/baidu-tracking.html", Lists.newArrayList());
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream outputStream) throws IOException {
        Map<String, Object> scopedBindings = Maps.newHashMap();
        scopedBindings.putAll(bindings);
        scopedBindings.put("script", baiduStatisticsScriptService.script());
        template().output(scopedBindings, outputStream);
    }
}
