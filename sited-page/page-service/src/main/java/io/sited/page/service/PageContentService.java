package io.sited.page.service;

import com.google.common.collect.ImmutableList;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.page.domain.PageContent;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author chi
 */
public class PageContentService {
    @Inject
    Repository<PageContent> repository;


    public PageContent getByPageId(String pageId) {
        return repository.query("SELECT t from PageContent t WHERE t.pageId=?0", pageId).findOne().orElseThrow(() -> new NotFoundException("missing content, pageId=" + pageId));
    }

    @Transactional
    public PageContent create(String pageId, String content, String requestBy) {
        PageContent pageContent = new PageContent();
        pageContent.id = UUID.randomUUID().toString();
        pageContent.pageId = pageId;
        pageContent.content = content;
        pageContent.createdTime = OffsetDateTime.now();
        pageContent.updatedTime = OffsetDateTime.now();
        pageContent.createdBy = requestBy;
        pageContent.updatedBy = requestBy;
        return repository.insert(pageContent);
    }

    @Transactional
    public PageContent update(String pageId, String content, String requestBy) {
        delete(pageId);

        PageContent pageContent = new PageContent();
        pageContent.id = UUID.randomUUID().toString();
        pageContent.pageId = pageId;
        pageContent.content = content;
        pageContent.createdTime = OffsetDateTime.now();
        pageContent.updatedTime = OffsetDateTime.now();
        pageContent.createdBy = requestBy;
        pageContent.updatedBy = requestBy;
        return repository.insert(pageContent);
    }


    @Transactional
    public void delete(String id) {
        repository.execute("DELETE FROM PageContent t WHERE t.pageId=?0", id);
    }

    @Transactional
    public void batchDelete(List<String> ids) {
        repository.batchDelete(ids);
    }

    public List<PageContent> batchGet(List<String> ids) {
        return repository.batchGet(ids);
    }

    public List<PageContent> batchGetByPageIds(List<String> pageIds) {
        if (pageIds.isEmpty()) {
            return ImmutableList.of();
        }
        Query<PageContent> query = repository.query("SELECT t FROM PageContent t WHERE t.pageId IN (");
        for (int i = 0; i < pageIds.size(); i++) {
            if (i != 0) {
                query.append(",");
            }
            String pageId = pageIds.get(i);
            query.append("?" + i, pageId);
        }
        query.append(")");
        return query.find();
    }
}
