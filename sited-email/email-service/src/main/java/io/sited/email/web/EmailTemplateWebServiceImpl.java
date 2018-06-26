package io.sited.email.web;

import io.sited.email.api.EmailTemplateWebService;
import io.sited.email.api.template.BatchDeleteRequest;
import io.sited.email.api.template.CreateEmailTemplateRequest;
import io.sited.email.api.template.EmailTemplateQuery;
import io.sited.email.api.template.EmailTemplateResponse;
import io.sited.email.api.template.UpdateEmailTemplateRequest;
import io.sited.email.domain.EmailTemplate;
import io.sited.email.service.EmailTemplateService;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author chi
 */
public class EmailTemplateWebServiceImpl implements EmailTemplateWebService {
    @Inject
    EmailTemplateService emailTemplateService;

    @Override
    public EmailTemplateResponse create(CreateEmailTemplateRequest request) {
        return response(emailTemplateService.create(request));
    }

    @Override
    public EmailTemplateResponse get(String id) {
        return response(emailTemplateService.get(id));
    }

    @Override
    public Optional<EmailTemplateResponse> findByName(String name) {
        Optional<EmailTemplate> emailTemplate = emailTemplateService.findByName(name);
        return emailTemplate.map(this::response);
    }

    @Override
    public QueryResponse<EmailTemplateResponse> find(EmailTemplateQuery query) {
        return emailTemplateService.find(query).map(this::response);
    }

    @Override
    public EmailTemplateResponse update(String id, UpdateEmailTemplateRequest request) {
        return response(emailTemplateService.update(id, request));
    }

    @Override
    public void delete(String id, String requestBy) {
        emailTemplateService.delete(id, requestBy);
    }

    @Override
    public void batchDelete(BatchDeleteRequest request) {
        request.ids.forEach(id -> {
            emailTemplateService.delete(id, request.requestBy);
        });
    }

    private EmailTemplateResponse response(EmailTemplate emailTemplate) {
        EmailTemplateResponse response = new EmailTemplateResponse();
        response.id = emailTemplate.id;
        response.name = emailTemplate.name;
        response.subject = emailTemplate.subject;
        response.content = emailTemplate.content;
        response.status = emailTemplate.status;
        response.createdTime = emailTemplate.createdTime;
        response.createdBy = emailTemplate.createdBy;
        response.updatedTime = emailTemplate.updatedTime;
        response.updatedBy = emailTemplate.updatedBy;
        return response;
    }
}
