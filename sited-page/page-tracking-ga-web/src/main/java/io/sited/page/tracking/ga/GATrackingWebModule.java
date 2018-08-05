package io.sited.page.tracking.ga;

import io.sited.page.tracking.ga.service.GAStatisticsScriptService;
import io.sited.page.tracking.ga.service.component.GATrackingComponent;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class GATrackingWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        bind(GAStatisticsScriptService.class).toInstance(new GAStatisticsScriptService(options("ga", GAStatisticsOptions.class).id));
        web().addComponent(requestInjection(new GATrackingComponent()));
    }
}
