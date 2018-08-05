package io.sited.pincode.service;

import com.google.common.base.Strings;
import io.sited.message.MessageHandler;
import io.sited.pincode.api.message.SendPinCodeMessage;
import io.sited.util.JSON;
import io.sited.util.i18n.MessageBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * @author chi
 */
public class PinCodeMessageHandler implements MessageHandler<SendPinCodeMessage> {
    private final Logger logger = LoggerFactory.getLogger(PinCodeMessageHandler.class);

    @Inject
    EmailSender emailSender;

    @Inject
    MessageBundle messageBundle;

    @Override
    public void handle(SendPinCodeMessage message) throws Throwable {
        logger.debug(JSON.toJSON(message));

        if (!Strings.isNullOrEmpty(message.email)) {
            SendEmailRequest request = new SendEmailRequest();
            request.to = message.email;
            request.subject = String.format(messageBundle.get("pincode.subject").orElse("your pincode is %s"), message.code);
            request.content = String.format(messageBundle.get("pincode.content").orElse("your pincode is %s"), message.code);
            request.mimeType = MimeType.TEXT;
            emailSender.send(request);
        }
    }
}
