package app.jweb.captcha.simple.service;


import app.jweb.captcha.simple.CaptchaOptions;
import app.jweb.captcha.web.CaptchaCode;
import app.jweb.web.SessionInfo;

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
