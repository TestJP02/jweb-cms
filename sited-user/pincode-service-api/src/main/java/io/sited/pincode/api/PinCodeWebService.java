package io.sited.pincode.api;

import io.sited.pincode.api.pincode.CreatePinCodeRequest;
import io.sited.pincode.api.pincode.PinCodeResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author chi
 */
@Path("/api/pincode")
public interface PinCodeWebService {

    @POST
    PinCodeResponse create(CreatePinCodeRequest request);
}
