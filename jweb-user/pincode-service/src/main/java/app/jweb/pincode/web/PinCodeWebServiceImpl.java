package app.jweb.pincode.web;

import app.jweb.pincode.api.PinCodeWebService;
import app.jweb.pincode.api.pincode.CreatePinCodeRequest;
import app.jweb.pincode.api.pincode.PinCodeResponse;
import app.jweb.pincode.service.PinCodeService;

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
