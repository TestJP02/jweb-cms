package io.sited.pincode.web;

import io.sited.pincode.api.PinCodeWebService;
import io.sited.pincode.api.pincode.CreatePinCodeRequest;
import io.sited.pincode.api.pincode.PinCodeResponse;
import io.sited.pincode.service.PinCodeService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PinCodeWebServiceImpl implements PinCodeWebService {
    @Inject
    PinCodeService pinCodeService;

    @Override
    public PinCodeResponse create(CreatePinCodeRequest request) {
        PinCodeResponse response = new PinCodeResponse();
        response.code = pinCodeService.create(request);
        return response;
    }
}
