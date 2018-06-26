package io.sited.page.statistics.baidu;

import io.sited.page.statistics.baidu.component.BaiduTrackingComponent;
import io.sited.page.statistics.baidu.service.BaiduStatisticsScriptService;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class BaiduTrackingWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        BaiduStatisticsScriptService baiduStatisticsScriptService = new BaiduStatisticsScriptService(options("baidu-statistics", BaiduStatisticsOptions.class).id);
        bind(BaiduStatisticsScriptService.class).toInstance(baiduStatisticsScriptService);
        web().addComponent(requestInjection(new BaiduTrackingComponent()));
    }
}
