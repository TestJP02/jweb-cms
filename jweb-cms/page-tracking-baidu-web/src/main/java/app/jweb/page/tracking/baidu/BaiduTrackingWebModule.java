package app.jweb.page.tracking.baidu;

import app.jweb.page.tracking.baidu.service.BaiduTrackingScriptService;
import app.jweb.page.tracking.baidu.service.component.BaiduTrackingComponent;
import app.jweb.page.tracking.baidu.service.processor.BaiduTrackingElementProcessor;
import app.jweb.web.AbstractWebModule;

/**
 * @author chi
 */
public class BaiduTrackingWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        BaiduTrackingScriptService baiduTrackingScriptService = new BaiduTrackingScriptService(options("baidu-tracking", BaiduTrackingOptions.class).id);
        bind(BaiduTrackingScriptService.class).toInstance(baiduTrackingScriptService);
        web().addComponent(requestInjection(new BaiduTrackingComponent()));
        web().addElementProcessor(new BaiduTrackingElementProcessor());
    }
}
