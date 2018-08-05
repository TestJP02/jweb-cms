package io.sited.page.tracking.ga.service.component;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import io.sited.page.tracking.ga.service.GAStatisticsScriptService;
import io.sited.template.Attributes;
import io.sited.template.Children;
import io.sited.web.AbstractWebComponent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class GATrackingComponent extends AbstractWebComponent {
    @Inject
    GAStatisticsScriptService gaStatisticsScriptService;

    public GATrackingComponent() {
        super("ga-tracking", "component/ga-tracking/ga-tracking.html", ImmutableList.of());
    }

    @Override
    public void output(Map<String, Object> bindings, Attributes attributes, Children children, OutputStream out) throws IOException {
        if (gaStatisticsScriptService.isEnabled()) {
            Map<String, Object> scopedBindings = Maps.newHashMap();
            scopedBindings.putAll(bindings);
            scopedBindings.put("script", gaStatisticsScriptService.script());
            template().output(scopedBindings, out);
        }
    }
}
