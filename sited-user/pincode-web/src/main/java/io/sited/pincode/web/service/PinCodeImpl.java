package io.sited.pincode.web.service;

import io.sited.pincode.web.PinCodeWebOptions;
import io.sited.pincode.web.PinCode;
import io.sited.web.SessionInfo;

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
