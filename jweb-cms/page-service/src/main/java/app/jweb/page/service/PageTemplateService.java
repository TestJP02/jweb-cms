package app.jweb.page.service;

import app.jweb.database.Repository;
import app.jweb.page.api.template.PageSectionView;
import app.jweb.page.domain.PageTemplate;
import app.jweb.util.JSON;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
public class PageTemplateService {
    @Inject
    Repository<PageTemplate> repository;

    public PageTemplate get(String pageId) {
        return repository.get(pageId);
    }

    @Transactional
    public PageTemplate create(String pageId, List<PageSectionView> sections, String requestBy) {
        PageTemplate pageTemplate = new PageTemplate();
        pageTemplate.pageId = pageId;
        pageTemplate.sections = sections == null ? null : JSON.toJSON(sections);
        pageTemplate.createdTime = OffsetDateTime.now();
        pageTemplate.updatedTime = OffsetDateTime.now();
        pageTemplate.createdBy = requestBy;
        pageTemplate.updatedBy = requestBy;
        return repository.insert(pageTemplate);
    }

    @Transactional
    public PageTemplate update(String pageId, List<PageSectionView> sections, String requestBy) {
        PageTemplate pageTemplate = get(pageId);
        pageTemplate.sections = sections == null ? null : JSON.toJSON(sections);
        pageTemplate.updatedTime = OffsetDateTime.now();
        pageTemplate.updatedBy = requestBy;
        return repository.update(pageId, pageTemplate);
    }

    @Transactional
    public void copySections(String fromPageId, String toPageId, String requestBy) {
        PageTemplate fromTemplate = get(fromPageId);
        PageTemplate pageTemplate = new PageTemplate();
        pageTemplate.pageId = toPageId;
        pageTemplate.sections = fromTemplate.sections;
        pageTemplate.updatedTime = OffsetDateTime.now();
        pageTemplate.updatedBy = requestBy;
        pageTemplate.createdTime = OffsetDateTime.now();
        pageTemplate.createdBy = requestBy;
        repository.insert(pageTemplate);
    }

    @Transactional
    public void replace(String fromPageId, String toPageId, String requestBy) {
        PageTemplate fromTemplate = get(fromPageId);
        PageTemplate pageTemplate = get(toPageId);
        pageTemplate.sections = fromTemplate.sections;
        pageTemplate.updatedTime = OffsetDateTime.now();
        pageTemplate.updatedBy = requestBy;
        repository.update(pageTemplate.pageId, pageTemplate);
    }

    @Transactional
    public void delete(String id) {
        repository.delete(id);
    }
}
