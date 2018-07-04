package io.sited.captcha.web;


import io.sited.captcha.web.impl.NoneCaptchaCodeProvider;
import io.sited.web.AbstractWebModule;

/**
 * @author chi
 */
public class CaptchaModule extends AbstractWebModule {
    @Override
    protected void configure() {
        web().bind(CaptchaCode.class, new NoneCaptchaCodeProvider());
    }
}
