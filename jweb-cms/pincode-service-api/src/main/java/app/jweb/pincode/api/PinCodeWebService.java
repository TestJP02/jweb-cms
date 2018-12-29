package app.jweb.pincode.api;

import app.jweb.pincode.api.pincode.CreatePinCodeRequest;
import app.jweb.pincode.api.pincode.PinCodeResponse;

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
