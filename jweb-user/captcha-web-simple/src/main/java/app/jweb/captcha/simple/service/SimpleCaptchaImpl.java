package app.jweb.captcha.simple.service;

import app.jweb.captcha.simple.CaptchaOptions;
import app.jweb.captcha.web.CaptchaCode;
import app.jweb.util.exception.Exceptions;
import app.jweb.web.SessionInfo;

import java.util.Optional;

/**
 * @author chi
 */
public class SimpleCaptchaImpl implements CaptchaCode {
    public static final String SESSION_KEY = "CAPTCHA_CODE";
    private final CaptchaOptions options;
    private final SessionInfo session;

    public SimpleCaptchaImpl(CaptchaOptions options, SessionInfo session) {
        this.options = options;
        this.session = session;
    }

    @Override
    public void delete() {
        session.delete(SESSION_KEY);
    }

    @Override
    public void validate(String code) {
        if (Boolean.TRUE.equals(options.enabled)) {
            Optional<String> captchaCode = session.get(SESSION_KEY);
            if (!captchaCode.isPresent() || !captchaCode.get().equals(code)) {
                throw Exceptions.badRequestException("captchaCode", "invalid captcha code");
            }
        }
    }
}
