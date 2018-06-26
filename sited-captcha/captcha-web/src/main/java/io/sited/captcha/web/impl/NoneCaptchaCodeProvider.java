package io.sited.captcha.web.impl;

import io.sited.captcha.web.CaptchaCode;

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
