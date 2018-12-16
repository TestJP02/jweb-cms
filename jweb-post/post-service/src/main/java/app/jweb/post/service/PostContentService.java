package app.jweb.post.service;

import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.post.domain.PostContent;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author chi
 */
public class PostContentService {
    @Inject
    Repository<PostContent> repository;


    public PostContent getByPostId(String postId) {
        return repository.query("SELECT t from PostContent t WHERE t.postId=?0", postId).findOne().orElseThrow(() -> new NotFoundException("missing content, postId=" + postId));
    }

    @Transactional
    public PostContent create(String postId, String content, String requestBy) {
        PostContent postContent = new PostContent();
        postContent.id = UUID.randomUUID().toString();
        postContent.postId = postId;
        postContent.content = content;
        postContent.createdTime = OffsetDateTime.now();
        postContent.updatedTime = OffsetDateTime.now();
        postContent.createdBy = requestBy;
        postContent.updatedBy = requestBy;
        return repository.insert(postContent);
    }

    @Transactional
    public PostContent update(String postId, String content, String requestBy) {
        delete(postId);

        PostContent postContent = new PostContent();
        postContent.id = UUID.randomUUID().toString();
        postContent.postId = postId;
        postContent.content = content;
        postContent.createdTime = OffsetDateTime.now();
        postContent.updatedTime = OffsetDateTime.now();
        postContent.createdBy = requestBy;
        postContent.updatedBy = requestBy;
        return repository.insert(postContent);
    }


    @Transactional
    public void delete(String id) {
        repository.execute("DELETE FROM PostContent t WHERE t.postId=?0", id);
    }

    @Transactional
    public void batchDelete(List<String> ids) {
        repository.batchDelete(ids);
    }

    public List<PostContent> batchGet(List<String> ids) {
        return repository.batchGet(ids);
    }

    public List<PostContent> batchGetByPostIds(List<String> postIds) {
        if (postIds.isEmpty()) {
            return ImmutableList.of();
        }
        Query<PostContent> query = repository.query("SELECT t FROM PostContent t WHERE t.postId IN (");
        for (int i = 0; i < postIds.size(); i++) {
            if (i != 0) {
                query.append(",");
            }
            String postId = postIds.get(i);
            query.append("?" + i, postId);
        }
        query.append(")");
        return query.find();
    }
}
