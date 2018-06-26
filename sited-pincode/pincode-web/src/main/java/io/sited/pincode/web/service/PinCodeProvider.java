package io.sited.pincode.web.service;

import io.sited.pincode.web.PinCode;
import io.sited.pincode.web.PinCodeWebOptions;
import io.sited.web.SessionInfo;

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
