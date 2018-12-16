package app.jweb.pincode.web.service;

import app.jweb.pincode.web.PinCode;
import app.jweb.pincode.web.PinCodeWebOptions;
import app.jweb.web.SessionInfo;

import java.util.Optional;

/**
 * @author chi
 */
public class PinCodeImpl implements PinCode {
    private final PinCodeWebOptions options;
    private final SessionInfo session;

    public PinCodeImpl(PinCodeWebOptions options, SessionInfo session) {
        this.options = options;
        this.session = session;
    }

    @Override
    public boolean validate(String code) {
        if (Boolean.TRUE.equals(options.pinCodeEnabled)) {
            Optional<String> pinCode = session.get("pinCode");
            return pinCode.isPresent() && pinCode.get().equals(code);
        }
        return true;
    }
}
