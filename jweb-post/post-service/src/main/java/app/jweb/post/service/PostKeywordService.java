package app.jweb.post.service;

import app.jweb.database.Repository;
import app.jweb.message.MessagePublisher;
import app.jweb.post.api.keyword.KeywordChangedMessage;
import app.jweb.post.domain.PostKeyword;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

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
public class PostKeywordService {
    @Inject
    Repository<PostKeyword> repository;
    @Inject
    MessagePublisher<KeywordChangedMessage> publisher;

    public PostKeyword get(String id) {
        return repository.get(id);
    }

    public Optional<PostKeyword> findByKeyword(String keyword) {
        return repository.query("SELECT t from PostKeyword t WHERE t.keyword=?0", keyword).findOne();
    }

    public List<PostKeyword> findByPath(String path) {
        return repository.query("SELECT t from PostKeyword t WHERE t.path=?0", path).find();
    }

    public List<PostKeyword> find() {
        return repository.query("SELECT t from PostKeyword t").find();
    }

    @Transactional
    public void update(String path, Set<String> keywords, String requestBy) {
        List<PostKeyword> origins = findByPath(path);
        Set<String> toDeleteIds = Sets.newHashSet();
        Set<String> toUpdateKeywords = Sets.newHashSet();
        Set<String> toInsertKeywords = Sets.newHashSet();
        Set<String> toDeleteKeywords = Sets.newHashSet();
        List<PostKeyword> toInsert = Lists.newArrayList();

        for (PostKeyword postKeyword : origins) {
            if (!keywords.contains(postKeyword.keyword)) {
                toDeleteIds.add(postKeyword.id);
                toDeleteKeywords.add(postKeyword.keyword);
            } else {
                toUpdateKeywords.add(postKeyword.keyword);
            }
        }
        for (String keyword : keywords) {
            if (toUpdateKeywords.contains(keyword)) continue;
            Optional<PostKeyword> optional = findByKeyword(keyword);
            if (optional.isPresent()) toUpdateKeywords.add(keyword);
            PostKeyword postKeyword = postKeyword(path, keyword, requestBy);
            toInsert.add(postKeyword);
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

    private PostKeyword postKeyword(String path, String keyword, String requestBy) {
        PostKeyword postKeyword = new PostKeyword();
        postKeyword.id = UUID.randomUUID().toString();
        postKeyword.keyword = keyword;
        postKeyword.path = path;
        postKeyword.createdBy = requestBy;
        postKeyword.updatedBy = requestBy;
        postKeyword.createdTime = OffsetDateTime.now();
        postKeyword.updatedTime = OffsetDateTime.now();
        return postKeyword;
    }

    @Transactional
    private void batchInsert(List<PostKeyword> list) {
        repository.batchInsert(list);
    }

    @Transactional
    private void batchUpdate(String path, Set<String> keywords, String requestBy) {
        List<Object> paramSet = Lists.newArrayList();
        paramSet.add(path);
        paramSet.add(OffsetDateTime.now());
        paramSet.add(requestBy);
        StringBuilder sql = new StringBuilder("UPDATE PostKeyword t SET t.path=?0,t.updatedTime=?1,t.updatedBy=?2 WHERE t.keyword IN (");
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
