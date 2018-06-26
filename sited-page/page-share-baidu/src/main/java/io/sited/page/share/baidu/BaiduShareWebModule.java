package io.sited.page.share.baidu;


import io.sited.page.share.baidu.component.BaiduShareComponent;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class BaiduShareWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        web().addComponent(requestInjection(new BaiduShareComponent()));
    }
}
