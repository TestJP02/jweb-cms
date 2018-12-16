package app.jweb.captcha.web;


import app.jweb.captcha.web.impl.NoneCaptchaCodeProvider;
import app.jweb.web.AbstractWebModule;

/**
 * @author chi
 */
public class CaptchaModule extends AbstractWebModule {
    @Override
    protected void configure() {
        web().bind(CaptchaCode.class, new NoneCaptchaCodeProvider());
    }
}
