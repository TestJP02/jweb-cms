package app.jweb.pincode.web.web.ajax;

import app.jweb.pincode.api.PinCodeWebService;
import app.jweb.pincode.api.pincode.CreatePinCodeRequest;
import app.jweb.pincode.api.pincode.PinCodeResponse;
import app.jweb.web.ClientInfo;
import app.jweb.web.SessionInfo;

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
