package io.sited.email.ses.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.google.common.collect.Lists;
import io.sited.email.EmailVendor;
import io.sited.email.api.email.MimeType;
import io.sited.email.api.email.SendEmailRequest;
import io.sited.email.api.email.SendEmailResponse;
import io.sited.email.api.email.SendEmailStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chi
 */
public class SESEmailVendorImpl implements EmailVendor {
    private final Logger logger = LoggerFactory.getLogger(SESEmailVendorImpl.class);
    private final AmazonSimpleEmailService amazonSimpleEmailService;

    public SESEmailVendorImpl(AmazonSimpleEmailService amazonSimpleEmailService) {
        this.amazonSimpleEmailService = amazonSimpleEmailService;
    }

    @Override
    public SendEmailResponse send(SendEmailRequest request) {
        try {
            SendEmailResult sendEmailResult = amazonSimpleEmailService.sendEmail(awsRequest(request));
            SendEmailResponse sendResponse = new SendEmailResponse();
            sendResponse.status = SendEmailStatus.SUCCESS;
            sendResponse.result = sendEmailResult.getMessageId();
            return sendResponse;
        } catch (Exception e) {
            logger.error("failed to send email, from={}, to={}, subject={}", request.from, request.to, request.subject, e);
            SendEmailResponse sendResponse = new SendEmailResponse();
            sendResponse.status = SendEmailStatus.SUCCESS;
            sendResponse.errorMessage = e.getMessage();
            return sendResponse;
        }
    }

    private com.amazonaws.services.simpleemail.model.SendEmailRequest awsRequest(SendEmailRequest sendEmailRequest) {
        com.amazonaws.services.simpleemail.model.SendEmailRequest awsRequest = new com.amazonaws.services.simpleemail.model.SendEmailRequest();
        awsRequest.setSource(sendEmailRequest.from);
        awsRequest.setDestination(new Destination(sendEmailRequest.to));
        Content subject = new Content(sendEmailRequest.subject);
        Body body = new Body();
        if (sendEmailRequest.mimeType == MimeType.HTML) {
            body.withHtml(new Content(sendEmailRequest.content));
        } else
            body.withText(new Content(sendEmailRequest.content));
        Message message = new Message(subject, body);
        awsRequest.setMessage(message);
        awsRequest.setReplyToAddresses(Lists.newArrayList(sendEmailRequest.replyTo));
        return awsRequest;
    }
}
