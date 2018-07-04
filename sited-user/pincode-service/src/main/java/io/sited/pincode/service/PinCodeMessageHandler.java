package io.sited.pincode.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.sited.email.api.EmailWebService;
import io.sited.email.api.email.SendTemplateEmailRequest;
import io.sited.message.MessageHandler;
import io.sited.pincode.api.message.SendPinCodeMessage;
import io.sited.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Collections;

/**
 * @author chi
 */
public class PinCodeMessageHandler implements MessageHandler<SendPinCodeMessage> {
    private final Logger logger = LoggerFactory.getLogger(PinCodeMessageHandler.class);

    @Inject
    EmailWebService emailWebService;

    @Override
    public void handle(SendPinCodeMessage message) throws Throwable {
        logger.debug(JSON.toJSON(message));

        if (!Strings.isNullOrEmpty(message.email)) {
            SendTemplateEmailRequest request = new SendTemplateEmailRequest();
            request.requestBy = "pincode-service";
            request.to = Lists.newArrayList(message.email);
            request.bindings = Collections.singletonMap("code", message.code);
            emailWebService.send("user-pincode", request);
        }
    }
}
