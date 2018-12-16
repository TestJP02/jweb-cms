package app.jweb.captcha.simple;

import app.jweb.captcha.simple.web.CaptchaController;
import app.jweb.captcha.simple.component.CaptchaComponent;
import app.jweb.captcha.simple.service.SimpleCaptchaCodeProvider;
import app.jweb.captcha.web.CaptchaCode;
import app.jweb.captcha.web.CaptchaModule;

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
