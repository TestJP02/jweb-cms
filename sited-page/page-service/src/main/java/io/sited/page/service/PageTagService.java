package io.sited.page.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.page.api.tag.BatchDeletePageTagRequest;
import io.sited.page.api.tag.CreatePageTagRequest;
import io.sited.page.api.tag.PageTagQuery;
import io.sited.page.api.tag.PageTagStatus;
import io.sited.page.api.tag.UpdatePageTagRequest;
import io.sited.page.domain.PageTag;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageTagService {
    @Inject
    Repository<PageTag> repository;

    public PageTag get(String id) {
        return repository.get(id);
    }

    public Optional<PageTag> findByName(String name) {
        return repository.query("SELECT t FROM PageTag t WHERE t.name=?0", name).findOne();
    }

    public QueryResponse<PageTag> find(PageTagQuery query) {
        Query<PageTag> tagQuery = repository.query("SELECT t FROM PageTag t WHERE 1=1");
        if (!Strings.isNullOrEmpty(query.name)) {
            tagQuery.append("t.name LIKE ?0", "%" + query.name + "%");
        }
        tagQuery.limit(query.page, query.limit);
        return tagQuery.findAll();
    }

    @Transactional
    public PageTag create(CreatePageTagRequest request) {
        Optional<PageTag> original = findByName(request.name);
        if (original.isPresent()) {
            return original.get();
        }

        PageTag pageTag = new PageTag();
        pageTag.id = UUID.randomUUID().toString();
        pageTag.name = request.name;
        pageTag.path = request.path;
        pageTag.totalTagged = 0;
        pageTag.status = PageTagStatus.ACTIVE;
        pageTag.createdTime = OffsetDateTime.now();
        pageTag.updatedTime = OffsetDateTime.now();
        pageTag.createdBy = request.requestBy;
        pageTag.updatedBy = request.requestBy;

        repository.insert(pageTag);
        return pageTag;
    }

    @Transactional
    public PageTag update(String id, UpdatePageTagRequest request) {
        PageTag pageTag = get(id);
        pageTag.name = request.name;
        pageTag.path = request.path;
        pageTag.totalTagged = request.totalTagged;
        pageTag.status = request.status;
        pageTag.updatedTime = OffsetDateTime.now();
        pageTag.updatedBy = request.requestBy;
        repository.update(id, pageTag);
        return pageTag;
    }

    @Transactional
    public void batchDelete(BatchDeletePageTagRequest request) {
        List<PageTag> pageTags = repository.batchGet(request.ids);
        List<String> toDelete = Lists.newArrayList();
        for (PageTag pageTag : pageTags) {
            if (pageTag.status == PageTagStatus.INACTIVE) {
                toDelete.add(pageTag.id);
            } else {
                pageTag.status = PageTagStatus.INACTIVE;
                pageTag.updatedBy = request.requestBy;
                pageTag.updatedTime = OffsetDateTime.now();
                repository.update(pageTag.id, pageTag);
            }
        }

        repository.batchDelete(toDelete);
    }

    @Transactional
    public void tag(List<String> tags, String requestBy) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
        Map<String, PageTag> found = findByNames(tags).stream().collect(Collectors.toMap(tag -> tag.name, tag -> tag));
        List<String> toIncreaseTagged = Lists.newArrayList();
        List<PageTag> toCreate = Lists.newArrayList();
        for (String tag : tags) {
            if (found.containsKey(tag)) {
                toIncreaseTagged.add(found.get(tag).id);
            } else {
                PageTag pageTag = new PageTag();
                pageTag.id = UUID.randomUUID().toString();
                pageTag.name = tag;
                pageTag.path = null;
                pageTag.totalTagged = 1;
                pageTag.updatedTime = OffsetDateTime.now();
                pageTag.updatedBy = requestBy;
                pageTag.createdTime = OffsetDateTime.now();
                pageTag.createdBy = requestBy;
                toCreate.add(pageTag);
            }
        }
        repository.batchInsert(toCreate);
        increaseTotalTagged(toIncreaseTagged, 1);
    }

    private List<PageTag> findByNames(List<String> tags) {
        Query<PageTag> query = repository.query("SELECT t FROM PageTag t WHERE t.name in(");
        for (int i = 0; i < tags.size(); i++) {
            if (i != 0) {
                query.append(",");
            }
            query.append("\'" + tags.get(i) + "\'");
        }
        query.append(")");
        return query.find();
    }

    @Transactional
    private void increaseTotalTagged(List<String> ids, int count) {
        if (ids.isEmpty()) {
            return;
        }
        StringBuilder b = new StringBuilder();
        b.append("UPDATE PageTag t SET t.totalTagged = t.totalTagged+").append(count);
        b.append(" WHERE t.id in(");
        for (int i = 0; i < ids.size(); i++) {
            if (i != 0) {
                b.append(",");
            }
            b.append("\'").append(ids.get(i)).append("\'");
        }
        b.append(")");
        repository.execute(b.toString());
    }

    @Transactional
    public void untag(List<String> tags, String updatedBy) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
        StringBuilder b = new StringBuilder();
        b.append("UPDATE PageTag SET t.totalTagged = t.totalTagged-1");
        b.append(" WHERE t.name in(");
        for (int i = 0; i < tags.size(); i++) {
            if (i != 0) {
                b.append(",");
            }
            b.append("\'").append(tags.get(i)).append("\'");
        }
        b.append(")");
        repository.execute(b.toString());
    }
}
