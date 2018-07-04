package io.sited.captcha.simple;

import io.sited.captcha.simple.component.CaptchaComponent;
import io.sited.captcha.simple.service.SimpleCaptchaCodeProvider;
import io.sited.captcha.simple.web.CaptchaController;
import io.sited.captcha.web.CaptchaCode;
import io.sited.captcha.web.CaptchaModule;

/**
 * @author chi
 */
public class SimpleCaptchaModule extends CaptchaModule {
    @Override
    protected void configure() {
        CaptchaOptions options = options("captcha", CaptchaOptions.class);
        bind(CaptchaOptions.class).toInstance(options);

        web().bind(CaptchaCode.class, SimpleCaptchaCodeProvider.class);
        web().controller(CaptchaController.class);
        web().addComponent(requestInjection(new CaptchaComponent()));
    }
}
