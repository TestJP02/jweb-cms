package app.jweb.page.service;

import app.jweb.database.Repository;
import app.jweb.page.domain.PageDraft;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PageDraftService {
    @Inject
    Repository<PageDraft> repository;

    public Optional<PageDraft> findByPageId(String pageId) {
        return repository.query("SELECT t FROM PageDraft WHERE t.pageId=?0", pageId).findOne();
    }

    public Optional<PageDraft> findByDraftId(String draftId) {
        return repository.query("SELECT t FROM PageDraft WHERE t.draftId=?0", draftId).findOne();
    }

    @Transactional
    public PageDraft create(String draftId, String pageId, String requestBy) {
        PageDraft pageDraft = new PageDraft();
        pageDraft.id = UUID.randomUUID().toString();
        pageDraft.draftId = draftId;
        pageDraft.pageId = pageId;
        pageDraft.createdTime = OffsetDateTime.now();
        pageDraft.createdBy = requestBy;
        repository.insert(pageDraft);
        return pageDraft;
    }

    @Transactional
    public void deleteByPageId(String pageId) {
        repository.execute("DELETE t FROM PageDraft WHERE t.pageId=?0", pageId);
    }
}
