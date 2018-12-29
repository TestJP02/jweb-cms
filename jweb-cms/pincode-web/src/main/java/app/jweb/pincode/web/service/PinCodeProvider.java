package app.jweb.pincode.web.service;

import app.jweb.pincode.web.PinCode;
import app.jweb.pincode.web.PinCodeWebOptions;
import app.jweb.web.SessionInfo;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author chi
 */
public class PinCodeProvider implements Provider<PinCode> {
    @Inject
    PinCodeWebOptions options;
    @Inject
    SessionInfo session;

    @Override
    public PinCode get() {
        return new PinCodeImpl(options, session);
    }
}
