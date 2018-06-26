package io.sited.email.web;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import io.sited.ApplicationException;
import io.sited.email.EmailVendor;
import io.sited.email.api.EmailWebService;
import io.sited.email.api.email.EmailQuery;
import io.sited.email.api.email.EmailResponse;
import io.sited.email.api.email.MimeType;
import io.sited.email.api.email.SendEmailRequest;
import io.sited.email.api.email.SendEmailResponse;
import io.sited.email.api.email.SendEmailStatus;
import io.sited.email.api.email.SendTemplateEmailRequest;
import io.sited.email.domain.EmailTemplate;
import io.sited.email.domain.EmailTracking;
import io.sited.email.service.EmailTemplateEngineManager;
import io.sited.email.service.EmailTemplateService;
import io.sited.email.service.EmailTrackingService;
import io.sited.message.MessagePublisher;
import io.sited.template.Template;
import io.sited.util.collection.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * @author chi
 */
public class EmailWebServiceImpl implements EmailWebService {
    private final Logger logger = LoggerFactory.getLogger(EmailWebServiceImpl.class);
    @Inject
    EmailVendor emailVendor;
    @Inject
    EmailTrackingService emailTrackingService;
    @Inject
    EmailTemplateService emailTemplateService;
    @Inject
    MessagePublisher<SendEmailRequest> publisher;
    @Inject
    EmailTemplateEngineManager templateEngineManager;

    @Override
    public SendEmailResponse send(SendEmailRequest request) {
        request.mimeType = MimeType.TEXT;
        return sendEmail(request);
    }

    @Override
    public SendEmailResponse send(String name, SendTemplateEmailRequest request) {
        EmailTemplate emailTemplate = emailTemplateService.findByName(name).orElseThrow(() -> new NotFoundException("missing template, name=" + name));
        SendEmailRequest sendEmailRequest = sendRequest(emailTemplate, request);
        return sendEmail(sendEmailRequest);
    }

    @Override
    public void sendAsync(SendEmailRequest request) {
        request.mimeType = MimeType.TEXT;
        publisher.publish(request);
    }

    @Override
    public void sendAsync(String name, SendTemplateEmailRequest request) {
        EmailTemplate emailTemplate = emailTemplateService.findByName(name).orElseThrow(() -> new NotFoundException("missing template, name=" + name));
        SendEmailRequest sendEmailRequest = sendRequest(emailTemplate, request);
        publisher.publish(sendEmailRequest);
    }

    @Override
    public EmailResponse get(String id) {
        return emailResponse(emailTrackingService.get(id).orElseThrow(() -> new NotFoundException("missing email,id=" + id)));
    }

    @Override
    public QueryResponse<EmailResponse> find(EmailQuery query) {
        return emailTrackingService.find(query).map(this::emailResponse);
    }

    private SendEmailResponse sendEmail(SendEmailRequest sendEmailRequest) {
        SendEmailResponse sendResponse = emailVendor.send(sendEmailRequest);
        if (sendResponse.status == SendEmailStatus.SUCCESS) {
            EmailTracking emailTracking = emailTrackingService.success(sendEmailRequest, sendResponse);
            return response(emailTracking);
        } else {
            EmailTracking emailTracking = emailTrackingService.failed(sendEmailRequest, sendResponse);
            return response(emailTracking);
        }
    }

    private SendEmailResponse response(EmailTracking emailTracking) {
        SendEmailResponse response = new SendEmailResponse();
        response.id = emailTracking.id;
        response.status = emailTracking.status;
        return response;
    }

    private SendEmailRequest sendRequest(EmailTemplate emailTemplate, SendTemplateEmailRequest request) {
        SendEmailRequest sendEmailRequest = new SendEmailRequest();
        sendEmailRequest.from = request.from;
        sendEmailRequest.to = request.to;
        sendEmailRequest.replyTo = request.replyTo;
        sendEmailRequest.subject = parse(request.bindings, "/" + emailTemplate.name + "-subject");
        sendEmailRequest.content = parse(request.bindings, "/" + emailTemplate.name + "-content");
        sendEmailRequest.mimeType = MimeType.HTML;
        sendEmailRequest.requestBy = request.requestBy;
        return sendEmailRequest;
    }

    private EmailResponse emailResponse(EmailTracking emailTracking) {
        EmailResponse response = new EmailResponse();
        response.id = emailTracking.id;
        response.from = emailTracking.from;
        response.replyTo = emailTracking.replyTo;
        response.to = emailTracking.to;
        response.subject = emailTracking.subject;
        response.content = emailTracking.content;
        response.status = emailTracking.status;
        response.errorMessage = emailTracking.errorMessage;
        response.createdTime = emailTracking.createdTime;
        response.createdBy = emailTracking.createdBy;
        return response;
    }

    private String parse(Map<String, String> bindings, String path) {
        Template template = templateEngineManager.get().template(path).orElseThrow(() -> new NotFoundException("missing template,path=" + path));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            template.output(Maps.newHashMap(bindings), outputStream);
            return new String(outputStream.toByteArray(), Charsets.UTF_8);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException("parse email template error,path={},", path, e);
        }
    }
}
