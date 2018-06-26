package io.sited.email.service;

import io.sited.email.EmailVendor;
import io.sited.email.api.email.SendEmailRequest;
import io.sited.email.api.email.SendEmailResponse;
import io.sited.email.api.email.SendEmailStatus;
import io.sited.message.MessageHandler;

import javax.inject.Inject;

/**
 * @author chi
 */
public class EmailMessageHandler implements MessageHandler<SendEmailRequest> {
    @Inject
    EmailVendor emailVendor;
    @Inject
    EmailTrackingService emailTrackingService;

    @Override
    public void handle(SendEmailRequest message) throws Throwable {
        SendEmailResponse sendResponse = emailVendor.send(message);
        if (sendResponse.status == SendEmailStatus.SUCCESS) {
            emailTrackingService.success(message, sendResponse);
        } else {
            emailTrackingService.failed(message, sendResponse);
        }
    }
}
