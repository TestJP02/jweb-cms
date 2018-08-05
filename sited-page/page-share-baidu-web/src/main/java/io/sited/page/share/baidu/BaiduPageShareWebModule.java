package io.sited.page.share.baidu;


import io.sited.page.share.baidu.component.BaiduShareButtonsComponent;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class BaiduPageShareWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        web().addComponent(requestInjection(new BaiduShareButtonsComponent()));
    }
}
