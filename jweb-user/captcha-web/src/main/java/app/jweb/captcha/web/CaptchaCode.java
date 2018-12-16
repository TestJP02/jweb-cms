package app.jweb.captcha.web;

/**
 * @author chi
 */
public interface CaptchaCode {
    void delete();

    void validate(String code);
}
