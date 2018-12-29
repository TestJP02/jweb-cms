package app.jweb.captcha.web.impl;

import app.jweb.captcha.web.CaptchaCode;

import javax.inject.Provider;

/**
 * @author chi
 */
public class NoneCaptchaCodeProvider implements Provider<CaptchaCode> {
    @Override
    public CaptchaCode get() {
        return new NoneCaptchaCodeImpl();
    }
}
