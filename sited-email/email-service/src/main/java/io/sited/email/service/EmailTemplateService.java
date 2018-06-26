package io.sited.email.service;

import com.google.common.base.Strings;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.email.api.template.CreateEmailTemplateRequest;
import io.sited.email.api.template.EmailTemplateQuery;
import io.sited.email.api.template.EmailTemplateStatus;
import io.sited.email.api.template.UpdateEmailTemplateRequest;
import io.sited.email.domain.EmailTemplate;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class EmailTemplateService {
    @Inject
    Repository<EmailTemplate> repository;

    @Transactional
    public EmailTemplate create(CreateEmailTemplateRequest request) {
        EmailTemplate emailTemplate = new EmailTemplate();
        emailTemplate.id = UUID.randomUUID().toString();
        emailTemplate.name = request.name;
        emailTemplate.subject = request.subject;
        emailTemplate.content = request.content;
        emailTemplate.createdTime = OffsetDateTime.now();
        emailTemplate.createdBy = request.requestBy;
        emailTemplate.updatedTime = OffsetDateTime.now();
        emailTemplate.updatedBy = request.requestBy;
        emailTemplate.status = EmailTemplateStatus.ACTIVE;
        repository.insert(emailTemplate);
        return emailTemplate;
    }

    public EmailTemplate get(String id) {
        return repository.get(id);
    }

    public Optional<EmailTemplate> findByName(String name) {
        return repository.query("SELECT t from EmailTemplate t WHERE t.name=?0", name).findOne();
    }

    public Optional<EmailTemplate> findByName(String name, String language) {
        Optional<EmailTemplate> template = repository.query("SELECT t from EmailTemplate t WHERE t.name=?0 AND t.language=?1", name, language).findOne();
        if (!template.isPresent()) {
            template = findByName(name);
        }
        return template;
    }

    public QueryResponse<EmailTemplate> find(EmailTemplateQuery emailTemplateQuery) {
        Query<EmailTemplate> query = repository.query("SELECT t from EmailTemplate t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(emailTemplateQuery.query)) {
            query.append("AND (t.name LIKE ?" + index++, "%" + emailTemplateQuery.query + "%");
            query.append("OR t.subject LIKE ?" + index++, "%" + emailTemplateQuery.query + "%");
            query.append(")");
        }
        if (emailTemplateQuery.status != null) {
            query.append("AND t.status=?" + index, emailTemplateQuery.status);
        }
        if (emailTemplateQuery.sortingField != null) {
            query.sort("t." + emailTemplateQuery.sortingField, emailTemplateQuery.desc);
        }
        return query.limit(emailTemplateQuery.page, emailTemplateQuery.limit).findAll();
    }

    @Transactional
    public EmailTemplate update(String id, UpdateEmailTemplateRequest request) {
        EmailTemplate emailTemplate = get(id);
        emailTemplate.name = request.name;
        emailTemplate.subject = request.subject;
        emailTemplate.content = request.content;
        emailTemplate.updatedBy = request.requestBy;
        emailTemplate.updatedTime = OffsetDateTime.now();
        repository.update(id, emailTemplate);
        return emailTemplate;
    }

    @Transactional
    public boolean delete(String id, String requestBy) {
        EmailTemplate emailTemplate = get(id);
        if (emailTemplate.status == EmailTemplateStatus.ACTIVE) {
            emailTemplate.status = EmailTemplateStatus.INACTIVE;
            emailTemplate.updatedBy = requestBy;
            emailTemplate.updatedTime = OffsetDateTime.now();
            repository.update(id, emailTemplate);
            return true;
        } else {
            return repository.delete(id);
        }
    }
}
