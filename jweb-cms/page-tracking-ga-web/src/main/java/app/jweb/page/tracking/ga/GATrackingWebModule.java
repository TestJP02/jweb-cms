package app.jweb.page.tracking.ga;

import app.jweb.page.tracking.ga.service.GAStatisticsScriptService;
import app.jweb.page.tracking.ga.service.component.GATrackingComponent;
import app.jweb.page.tracking.ga.service.processor.GATrackingElementProcessor;
import app.jweb.web.AbstractWebModule;

/**
 * @author chi
 */
public class GATrackingWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        bind(GAStatisticsScriptService.class).toInstance(new GAStatisticsScriptService(options("ga", GAStatisticsOptions.class).id));
        web().addElementProcessor(new GATrackingElementProcessor());
        web().addComponent(requestInjection(new GATrackingComponent()));
    }
}
