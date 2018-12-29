package app.jweb.page.share.baidu;


import app.jweb.page.share.baidu.component.BaiduShareButtonsComponent;
import app.jweb.web.AbstractWebModule;

/**
 * @author chi
 */
public class BaiduPageShareWebModule extends AbstractWebModule {
    @Override
    protected void configure() {
        web().addComponent(requestInjection(new BaiduShareButtonsComponent()));
    }
}
