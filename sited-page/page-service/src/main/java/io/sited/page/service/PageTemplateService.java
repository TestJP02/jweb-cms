package io.sited.page.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.message.MessagePublisher;
import io.sited.page.api.template.BatchCreateTemplateRequest;
import io.sited.page.api.template.CreateTemplateRequest;
import io.sited.page.api.template.TemplateChangedMessage;
import io.sited.page.api.template.TemplateQuery;
import io.sited.page.api.template.TemplateStatus;
import io.sited.page.api.template.UpdateTemplateRequest;
import io.sited.page.domain.PageTemplate;
import io.sited.util.JSON;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PageTemplateService {
    @Inject
    Repository<PageTemplate> repository;
    @Inject
    MessagePublisher<TemplateChangedMessage> publisher;

    public PageTemplate get(String id) {
        return repository.get(id);
    }

    public Optional<PageTemplate> findByPath(String path) {
        return repository.query("SELECT t FROM PageTemplate t WHERE t.path=?0", path).findOne();
    }

    public QueryResponse<PageTemplate> find(TemplateQuery templateQuery) {
        Query<PageTemplate> query = repository.query("SELECT t FROM PageTemplate t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(templateQuery.path)) {
            query.append("AND t.path LIKE ?" + index++, "%" + templateQuery.path + "%");
        }
        if (templateQuery.status != null) {
            query.append("AND t.status=?" + index, templateQuery.status);
        }
        query.limit(templateQuery.page, templateQuery.limit);
        if (templateQuery.sortingField != null) {
            query.sort("t." + templateQuery.sortingField, templateQuery.desc);
        }
        return query.findAll();
    }

    @Transactional
    public PageTemplate create(CreateTemplateRequest request) {
        Optional<PageTemplate> templateOptional = findByPath(request.path);
        if (templateOptional.isPresent()) {
            PageTemplate template = templateOptional.get();
            template.displayName = request.displayName;
            template.updatedTime = OffsetDateTime.now();
            template.updatedBy = request.requestBy;
            template.type = request.type;

            repository.update(template.id, template);
            return template;
        }

        PageTemplate pageTemplate = new PageTemplate();
        pageTemplate.id = UUID.randomUUID().toString();
        pageTemplate.path = request.path;
        pageTemplate.displayName = request.displayName;
        pageTemplate.type = request.type;
        pageTemplate.sections = request.sections == null ? null : JSON.toJSON(request.sections);
        pageTemplate.createdTime = OffsetDateTime.now();
        pageTemplate.updatedTime = OffsetDateTime.now();
        pageTemplate.createdBy = request.requestBy;
        pageTemplate.updatedBy = request.requestBy;
        pageTemplate.status = TemplateStatus.ACTIVE;
        repository.insert(pageTemplate);

        TemplateChangedMessage message = new TemplateChangedMessage();
        message.id = pageTemplate.id;
        message.path = pageTemplate.path;
        message.status = TemplateStatus.ACTIVE;
        publisher.publish(message);

        return pageTemplate;
    }

    @Transactional
    public void batchCreate(BatchCreateTemplateRequest request) {
        List<PageTemplate> templates = Lists.newArrayList();
        for (BatchCreateTemplateRequest.TemplateView template : request.templates) {
            Optional<PageTemplate> templateOptional = findByPath(template.path);
            if (!templateOptional.isPresent()) {
                PageTemplate pageTemplate = new PageTemplate();
                pageTemplate.id = UUID.randomUUID().toString();
                pageTemplate.path = template.path;
                pageTemplate.displayName = template.displayName;
                pageTemplate.type = template.type;
                pageTemplate.sections = template.sections == null ? null : JSON.toJSON(template.sections);
                pageTemplate.createdTime = OffsetDateTime.now();
                pageTemplate.updatedTime = OffsetDateTime.now();
                pageTemplate.createdBy = template.requestBy;
                pageTemplate.updatedBy = template.requestBy;
                pageTemplate.status = TemplateStatus.ACTIVE;
                templates.add(pageTemplate);
            }
        }
        repository.batchInsert(templates);
    }

    @Transactional
    public PageTemplate update(String id, UpdateTemplateRequest request) {
        PageTemplate pageTemplate = get(id);
        pageTemplate.displayName = request.displayName;
        pageTemplate.type = request.type;
        pageTemplate.sections = request.sections == null ? null : JSON.toJSON(request.sections);
        pageTemplate.updatedTime = OffsetDateTime.now();
        pageTemplate.updatedBy = request.requestBy;
        repository.update(id, pageTemplate);

        TemplateChangedMessage message = new TemplateChangedMessage();
        message.id = pageTemplate.id;
        message.path = pageTemplate.path;
        message.status = TemplateStatus.ACTIVE;
        publisher.publish(message);

        return pageTemplate;
    }

    @Transactional
    public void delete(String id, String requestBy) {
        PageTemplate pageTemplate = repository.get(id);
        if (pageTemplate.status == TemplateStatus.INACTIVE) {
            repository.delete(id);

            TemplateChangedMessage message = new TemplateChangedMessage();
            message.id = pageTemplate.id;
            message.path = pageTemplate.path;
            message.status = null;
            publisher.publish(message);
        } else {
            pageTemplate.status = TemplateStatus.INACTIVE;
            pageTemplate.updatedTime = OffsetDateTime.now();
            pageTemplate.updatedBy = requestBy;
            repository.update(id, pageTemplate);

            TemplateChangedMessage message = new TemplateChangedMessage();
            message.id = pageTemplate.id;
            message.path = pageTemplate.path;
            message.status = TemplateStatus.INACTIVE;
            publisher.publish(message);
        }
    }


}
