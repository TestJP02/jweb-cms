package io.sited.page.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.sited.database.Repository;
import io.sited.message.MessagePublisher;
import io.sited.page.api.keyword.KeywordChangedMessage;
import io.sited.page.domain.PageKeyword;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * @author chi
 */
public class PageKeywordService {
    @Inject
    Repository<PageKeyword> repository;
    @Inject
    MessagePublisher<KeywordChangedMessage> publisher;

    public PageKeyword get(String id) {
        return repository.get(id);
    }

    public Optional<PageKeyword> findByKeyword(String keyword) {
        return repository.query("SELECT t from PageKeyword t WHERE t.keyword=?0", keyword).findOne();
    }

    public List<PageKeyword> findByPath(String path) {
        return repository.query("SELECT t from PageKeyword t WHERE t.path=?0", path).find();
    }

    public List<PageKeyword> find() {
        return repository.query("SELECT t from PageKeyword t").find();
    }

    @Transactional
    public void update(String path, Set<String> keywords, String requestBy) {
        List<PageKeyword> origins = findByPath(path);
        Set<String> toDeleteIds = Sets.newHashSet();
        Set<String> toUpdateKeywords = Sets.newHashSet();
        Set<String> toInsertKeywords = Sets.newHashSet();
        Set<String> toDeleteKeywords = Sets.newHashSet();
        List<PageKeyword> toInsert = Lists.newArrayList();

        for (PageKeyword pageKeyword : origins) {
            if (!keywords.contains(pageKeyword.keyword)) {
                toDeleteIds.add(pageKeyword.id);
                toDeleteKeywords.add(pageKeyword.keyword);
            } else {
                toUpdateKeywords.add(pageKeyword.keyword);
            }
        }
        for (String keyword : keywords) {
            if (toUpdateKeywords.contains(keyword)) continue;
            Optional<PageKeyword> optional = findByKeyword(keyword);
            if (optional.isPresent()) toUpdateKeywords.add(keyword);
            PageKeyword pageKeyword = pageKeyword(path, keyword, requestBy);
            toInsert.add(pageKeyword);
            toInsertKeywords.add(keyword);
        }

        boolean changed = false;

        if (!toInsert.isEmpty()) {
            batchInsert(toInsert);
            changed = true;
        }
        if (!toUpdateKeywords.isEmpty()) {
            batchUpdate(path, toUpdateKeywords, requestBy);
            changed = true;
        }
        if (!toDeleteIds.isEmpty()) {
            batchDrop(toDeleteIds);
            changed = true;
        }

        if (changed) {
            KeywordChangedMessage message = new KeywordChangedMessage();
            message.path = path;
            message.toInsertKeywords = toInsertKeywords;
            message.toUpdateKeywords = toUpdateKeywords;
            message.toDeleteKeywords = toDeleteKeywords;
            publisher.publish(message);
        }
    }

    private PageKeyword pageKeyword(String path, String keyword, String requestBy) {
        PageKeyword pageKeyword = new PageKeyword();
        pageKeyword.id = UUID.randomUUID().toString();
        pageKeyword.keyword = keyword;
        pageKeyword.path = path;
        pageKeyword.createdBy = requestBy;
        pageKeyword.updatedBy = requestBy;
        pageKeyword.createdTime = OffsetDateTime.now();
        pageKeyword.updatedTime = OffsetDateTime.now();
        return pageKeyword;
    }

    @Transactional
    private void batchInsert(List<PageKeyword> list) {
        repository.batchInsert(list);
    }

    @Transactional
    private void batchUpdate(String path, Set<String> keywords, String requestBy) {
        List<Object> paramSet = Lists.newArrayList();
        paramSet.add(path);
        paramSet.add(OffsetDateTime.now());
        paramSet.add(requestBy);
        StringBuilder sql = new StringBuilder("UPDATE PageKeyword t SET t.path=?0,t.updatedTime=?1,t.updatedBy=?2 WHERE t.keyword IN (");
        int index = 3;
        for (String keyword : keywords) {
            sql.append('?').append(index++);
            if (index < keywords.size() - 1) {
                sql.append(',');
            }
            paramSet.add(keyword);
            index++;
        }
        sql.append(')');
        repository.execute(sql.toString(), paramSet.toArray(new Object[0]));
    }

    @Transactional
    private void batchDrop(Set<String> dropIds) {
        repository.batchDelete(Lists.newArrayList(dropIds));
    }

}
