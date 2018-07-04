package io.sited.page.archive.service;

import com.google.common.base.Strings;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.page.archive.api.archive.PageArchiveQuery;
import io.sited.page.archive.domain.PageArchive;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PageArchiveService {
    @Inject
    Repository<PageArchive> repository;

    public PageArchive get(String id) {
        return repository.get(id);
    }

    public Optional<PageArchive> findByName(String name) {
        return repository.query("SELECT t FROM PageArchive t WHERE t.name=?0", name).findOne();
    }

    public QueryResponse<PageArchive> find(PageArchiveQuery pageArchiveQuery) {
        Query<PageArchive> query = repository.query("SELECT t FROM PageArchive t WHERE 1=1");
        int index = 0;
        if (pageArchiveQuery.startTime != null) {
            query.append("t.timestamp>=?" + index++, pageArchiveQuery.startTime);
        }
        if (pageArchiveQuery.endTime != null) {
            query.append("t.timestamp<?" + index, pageArchiveQuery.endTime);
        }
        if (!Strings.isNullOrEmpty(pageArchiveQuery.sortingField)) {
            query.sort(pageArchiveQuery.sortingField, pageArchiveQuery.desc);
        } else {
            query.sort("t.name", true);
        }
        query.limit(pageArchiveQuery.page, pageArchiveQuery.limit);
        return query.findAll();
    }

    @Transactional
    private PageArchive create(OffsetDateTime timestamp, String requestBy) {
        PageArchive pageArchive = new PageArchive();
        pageArchive.id = UUID.randomUUID().toString();
        pageArchive.name = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        pageArchive.timestamp = timestamp;
        pageArchive.count = 1;
        pageArchive.createdTime = OffsetDateTime.now();
        pageArchive.updatedTime = OffsetDateTime.now();
        pageArchive.createdBy = requestBy;
        pageArchive.updatedBy = requestBy;
        repository.insert(pageArchive);
        return pageArchive;
    }

    @Transactional
    public void increase(OffsetDateTime publishTime, String requestBy) {
        String name = publishTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        int updated = increaseCount(name, requestBy);
        if (updated == 0) {
            create(publishTime, requestBy);
        }
    }

    @Transactional
    private int increaseCount(String name, String requestBy) {
        return repository.execute("UPDATE PageArchive t SET t.count=t.count+1,t.updatedBy=?0,t.updatedTime=?1 WHERE t.name=?2", requestBy, OffsetDateTime.now(), name);
    }

    @Transactional
    public void decrease(OffsetDateTime publishTime, String requestBy) {
        String name = publishTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        decreaseCount(name, requestBy);
    }

    @Transactional
    private int decreaseCount(String name, String requestBy) {
        return repository.execute("UPDATE PageArchive t SET t.count=t.count-1,t.updatedBy=?0,t.updatedTime=?1 WHERE t.name=?2", requestBy, OffsetDateTime.now(), name);
    }
}
