package io.sited.page.service;

import com.google.common.base.Strings;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.page.api.archive.PageArchiveQuery;
import io.sited.page.domain.PageArchive;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
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

    public Optional<PageArchive> findByYearAndMonth(Integer year, Integer month) {
        return repository.query("SELECT t FROM PageArchive t WHERE t.year=?0 AND t.month=?1", year, month).findOne();
    }

    public QueryResponse<PageArchive> find(PageArchiveQuery pageArchiveQuery) {
        Query<PageArchive> query = repository.query("SELECT t FROM PageArchive t WHERE 1=1");
        int index = 0;
        if (pageArchiveQuery.year != null) {
            query.append("t.year=?" + index++, pageArchiveQuery.year);
        }
        if (pageArchiveQuery.month != null) {
            query.append("t.month=?" + index, pageArchiveQuery.month);
        }
        if (!Strings.isNullOrEmpty(pageArchiveQuery.sortingField)) {
            query.sort(pageArchiveQuery.sortingField, pageArchiveQuery.desc);
        } else {
            query.sort("t.year", true);
            query.sort("t.month", true);
        }
        query.limit(pageArchiveQuery.page, pageArchiveQuery.limit);
        return query.findAll();
    }

    @Transactional
    private PageArchive create(Integer year, Integer month, String requestBy) {
        PageArchive pageArchive = new PageArchive();
        pageArchive.id = UUID.randomUUID().toString();
        pageArchive.year = year;
        pageArchive.month = month;
        pageArchive.count = 0;
        pageArchive.createdTime = OffsetDateTime.now();
        pageArchive.updatedTime = OffsetDateTime.now();
        pageArchive.createdBy = requestBy;
        pageArchive.updatedBy = requestBy;
        repository.insert(pageArchive);
        return pageArchive;
    }

    @Transactional
    public boolean increase(OffsetDateTime publishTime, String requestBy) {
        Integer year = publishTime.getYear();
        Integer month = publishTime.getMonthValue();
        PageArchive pageArchive = findByYearAndMonth(year, month).orElse(create(year, month, requestBy));
        return increase(pageArchive.id, requestBy);
    }

    @Transactional
    private boolean increase(String id, String requestBy) {
        return repository.execute("UPDATE PageArchive t SET t.count=t.count+1,t.updatedBy=?0,t.updatedTime=?1 WHERE t.id=?2", requestBy, OffsetDateTime.now(), id) > 0;
    }

    @Transactional
    public boolean decrease(OffsetDateTime publishTime, String requestBy) {
        Integer year = publishTime.getYear();
        Integer month = publishTime.getMonthValue();
        PageArchive pageArchive = findByYearAndMonth(year, month).orElse(create(year, month, requestBy));
        return decrease(pageArchive.id, requestBy);
    }

    @Transactional
    private boolean decrease(String id, String requestBy) {
        return repository.execute("UPDATE PageArchive t SET t.count=t.count-1,t.updatedBy=?0,t.updatedTime=?1 WHERE t.id=?2", requestBy, OffsetDateTime.now(), id) > 0;
    }
}
