package io.sited.pincode.web.web.ajax;

import io.sited.pincode.api.PinCodeWebService;
import io.sited.pincode.api.pincode.CreatePinCodeRequest;
import io.sited.pincode.api.pincode.PinCodeResponse;
import io.sited.web.ClientInfo;
import io.sited.web.SessionInfo;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.time.OffsetDateTime;
import java.util.Optional;

/**
 * @author chi
 */
@Path("/web/api/pincode")
public class PinCodeAJAXController {
    public static final String PIN_CODE_COUNT_DOWN_SESSION_KEY = "PinCodeCountDown";
    @Inject
    PinCodeWebService pinCodeWebService;
    @Inject
    ClientInfo clientInfo;
    @Inject
    SessionInfo sessionInfo;

    @POST
    public void create(CreatePinCodeAJAXRequest createPinCodeAJAXRequest) {
        CreatePinCodeRequest createPinCodeRequest = new CreatePinCodeRequest();
        createPinCodeRequest.email = createPinCodeAJAXRequest.email;
        createPinCodeRequest.phone = createPinCodeAJAXRequest.phone;
        createPinCodeRequest.ip = clientInfo.ip();
        createPinCodeRequest.requestBy = "pincode-web";
        Optional<String> optional = sessionInfo.get(PIN_CODE_COUNT_DOWN_SESSION_KEY);
        if (optional.isPresent()) {
            Long time = Long.valueOf(optional.get());
            if (OffsetDateTime.now().toEpochSecond() <= time) {
                throw new BadRequestException("Too often");
            } else {
                sessionInfo.delete(PIN_CODE_COUNT_DOWN_SESSION_KEY);
            }
        }
        PinCodeResponse response = pinCodeWebService.create(createPinCodeRequest);
        sessionInfo.put("pinCode", response.code);
        long interval = OffsetDateTime.now().toEpochSecond() + 60;
        sessionInfo.put(PIN_CODE_COUNT_DOWN_SESSION_KEY, String.valueOf(interval));
    }
}
