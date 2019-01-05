package app.jweb.page.service;

import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.message.MessagePublisher;
import app.jweb.page.api.page.BatchCreatePageRequest;
import app.jweb.page.api.page.CreatePageRequest;
import app.jweb.page.api.page.PageChangedMessage;
import app.jweb.page.api.page.PageQuery;
import app.jweb.page.api.page.PageSectionView;
import app.jweb.page.api.page.PageStatus;
import app.jweb.page.api.page.UpdatePageRequest;
import app.jweb.page.domain.PageTemplate;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import app.jweb.util.type.Types;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

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
    MessagePublisher<PageChangedMessage> publisher;

    public PageTemplate get(String id) {
        return repository.get(id);
    }

    public Optional<PageTemplate> findByTemplatePath(String templatePath) {
        return repository.query("SELECT t FROM PageTemplate t WHERE t.templatePath=?0", templatePath).findOne();
    }

    public Optional<PageTemplate> findByPath(String path) {
        return repository.query("SELECT t FROM PageTemplate t WHERE t.path=?0", path).findOne();
    }

    public QueryResponse<PageTemplate> find(PageQuery pageQuery) {
        Query<PageTemplate> query = repository.query("SELECT t FROM PageTemplate t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(pageQuery.templatePath)) {
            query.append("AND t.path LIKE ?" + index++, "%" + pageQuery.templatePath + "%");
        }
        if (pageQuery.status != null) {
            query.append("AND t.status=?" + index, pageQuery.status);
        }
        query.limit(pageQuery.page, pageQuery.limit);
        if (pageQuery.sortingField != null) {
            query.sort("t." + pageQuery.sortingField, pageQuery.desc);
        } else {
            query.sort("t.updatedTime", true);
        }
        return query.findAll();
    }

    @Transactional
    public PageTemplate create(CreatePageRequest request) {
        Optional<PageTemplate> templateOptional = findByTemplatePath(request.templatePath);
        if (templateOptional.isPresent()) {
            PageTemplate template = templateOptional.get();
            template.title = request.title;
            template.description = request.description;
            template.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
            template.updatedTime = OffsetDateTime.now();
            template.updatedBy = request.requestBy;
            template.type = request.type;

            repository.update(template.id, template);
            return template;
        }

        PageTemplate pageTemplate = new PageTemplate();
        pageTemplate.id = UUID.randomUUID().toString();
        pageTemplate.userId = request.userId;
        pageTemplate.path = request.path;
        pageTemplate.templatePath = request.templatePath;
        pageTemplate.title = request.title;
        pageTemplate.description = request.description;
        pageTemplate.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
        pageTemplate.type = request.type;
        pageTemplate.sections = request.sections == null ? null : JSON.toJSON(request.sections);
        pageTemplate.createdTime = OffsetDateTime.now();
        pageTemplate.updatedTime = OffsetDateTime.now();
        pageTemplate.createdBy = request.requestBy;
        pageTemplate.updatedBy = request.requestBy;
        pageTemplate.status = PageStatus.ACTIVE;
        repository.insert(pageTemplate);

        notifyChangedMessage(pageTemplate);

        return pageTemplate;
    }

    @Transactional
    public void batchCreate(BatchCreatePageRequest request) {
        List<PageTemplate> templates = Lists.newArrayList();
        for (BatchCreatePageRequest.TemplateView template : request.templates) {
            Optional<PageTemplate> templateOptional = findByTemplatePath(template.templatePath);
            if (!templateOptional.isPresent()) {
                PageTemplate pageTemplate = new PageTemplate();
                pageTemplate.id = UUID.randomUUID().toString();
                pageTemplate.path = template.path;
                pageTemplate.userId = template.userId;
                pageTemplate.templatePath = template.templatePath;
                pageTemplate.title = template.title;
                pageTemplate.description = template.description;
                pageTemplate.tags = template.tags == null ? null : Joiner.on(';').join(template.tags);
                pageTemplate.type = template.type;
                pageTemplate.sections = template.sections == null ? null : JSON.toJSON(template.sections);
                pageTemplate.createdTime = OffsetDateTime.now();
                pageTemplate.updatedTime = OffsetDateTime.now();
                pageTemplate.createdBy = template.requestBy;
                pageTemplate.updatedBy = template.requestBy;
                pageTemplate.status = PageStatus.ACTIVE;
                templates.add(pageTemplate);
            }
        }
        repository.batchInsert(templates);
    }

    @Transactional
    public PageTemplate update(String id, UpdatePageRequest request) {
        PageTemplate pageTemplate = get(id);
        pageTemplate.path = request.path;
        pageTemplate.templatePath = request.templatePath;
        pageTemplate.title = request.title;
        pageTemplate.description = request.description;
        pageTemplate.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
        pageTemplate.type = request.type;
        pageTemplate.sections = request.sections == null ? null : JSON.toJSON(request.sections);
        pageTemplate.updatedTime = OffsetDateTime.now();
        pageTemplate.updatedBy = request.requestBy;
        repository.update(id, pageTemplate);

        notifyChangedMessage(pageTemplate);

        return pageTemplate;
    }

    private void notifyChangedMessage(PageTemplate template) {
        PageChangedMessage message = new PageChangedMessage();
        message.id = template.id;
        message.userId = template.userId;
        message.path = template.path;
        message.templatePath = template.templatePath;
        message.title = template.title;
        message.description = template.description;
        message.tags = template.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(template.tags);
        message.type = template.type;
        message.sections = template.sections == null ? null : JSON.fromJSON(template.sections, Types.generic(List.class, PageSectionView.class));
        message.status = template.status;
        message.createdTime = OffsetDateTime.now();
        message.createdBy = template.createdBy;
        message.updatedTime = OffsetDateTime.now();
        message.updatedBy = template.updatedBy;
        publisher.publish(message);
    }

    @Transactional
    public void delete(String id, String requestBy) {
        PageTemplate pageTemplate = repository.get(id);
        if (pageTemplate.status == PageStatus.INACTIVE) {
            repository.delete(id);
        } else {
            pageTemplate.status = PageStatus.INACTIVE;
            pageTemplate.updatedTime = OffsetDateTime.now();
            pageTemplate.updatedBy = requestBy;
            repository.update(id, pageTemplate);

            notifyChangedMessage(pageTemplate);
        }
    }
}
