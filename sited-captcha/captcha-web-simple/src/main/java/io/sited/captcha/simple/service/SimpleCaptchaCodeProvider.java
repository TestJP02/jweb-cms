package io.sited.captcha.simple.service;


import io.sited.captcha.simple.CaptchaOptions;
import io.sited.captcha.web.CaptchaCode;
import io.sited.web.SessionInfo;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author chi
 */
public class SimpleCaptchaCodeProvider implements Provider<CaptchaCode> {
    @Inject
    CaptchaOptions options;

    @Inject
    SessionInfo sessionInfo;

    @Override
    public CaptchaCode get() {
        return new SimpleCaptchaImpl(options, sessionInfo);
    }
}
