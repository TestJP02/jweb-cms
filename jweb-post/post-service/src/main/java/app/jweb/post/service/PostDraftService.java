package app.jweb.post.service;

import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.post.api.draft.CreateDraftRequest;
import app.jweb.post.api.draft.DraftQuery;
import app.jweb.post.api.draft.UpdateDraftRequest;
import app.jweb.post.api.post.PostStatus;
import app.jweb.post.domain.PostDraft;
import app.jweb.post.util.Markdown;
import app.jweb.post.util.URLs;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PostDraftService {
    @Inject
    Repository<PostDraft> repository;
    @Inject
    PostCategoryService postCategoryService;

    public PostDraft get(String id) {
        return repository.get(id);
    }

    public Optional<PostDraft> findByPath(String path) {
        return repository.query("SELECT t FROM PostDraft t WHERE t.path=?0", path).findOne();
    }

    public Optional<PostDraft> findByPostId(String postId) {
        return repository.query("SELECT t FROM PostDraft t WHERE t.postId=?0", postId).findOne();
    }

    public QueryResponse<PostDraft> find(DraftQuery draftQuery) {
        int index = 0;
        Query<PostDraft> query = repository.query("SELECT t FROM PostDraft t WHERE 1=1");
        if (!Strings.isNullOrEmpty(draftQuery.query)) {
            query.append("AND (t.title LIKE ?" + index++ + " OR t.path LIKE ?" + index++ + ")", "%" + draftQuery.query + "%", "%" + draftQuery.query + "%");
        }
        if (!Strings.isNullOrEmpty(draftQuery.categoryId)) {
            List<String> ids = postCategoryService.childrenIds(draftQuery.categoryId);
            query.append("AND t.categoryId IN (");
            for (int i = 0; i < ids.size(); i++) {
                if (i != 0) query.append(",");
                String categoryId = ids.get(i);
                query.append("?" + index++, categoryId);
                index++;
            }
            query.append(")");
        }

        if (draftQuery.status != null) {
            query.append("AND t.status=?" + index, draftQuery.status);
        }

        query.limit(draftQuery.page, draftQuery.limit);
        if (!Strings.isNullOrEmpty(draftQuery.sortingField)) {
            query.sort("t." + draftQuery.sortingField, draftQuery.desc);
        }
        return query.findAll();
    }

    @Transactional
    public PostDraft update(String id, UpdateDraftRequest request) {
        PostDraft draft = get(id);
        draft.categoryId = request.categoryId;
        draft.path = URLs.normalize(request.path);
        draft.templatePath = request.templatePath;
        draft.title = request.title;
        draft.description = request.description;
        draft.content = request.content;
        draft.tags = request.tags == null || request.tags.isEmpty() ? null : Joiner.on(';').join(request.tags);
        draft.keywords = request.keywords == null || request.keywords.isEmpty() ? null : Joiner.on(';').join(request.keywords);
        draft.fields = request.fields == null ? null : JSON.toJSON(request.fields);
        draft.imageURL = request.imageURL;
        draft.updatedTime = OffsetDateTime.now();
        draft.updatedBy = request.requestBy;
        draft.topFixed = request.topFixed != null && request.topFixed;
        repository.update(draft.id, draft);
        return draft;
    }

    @Transactional
    public PostDraft create(CreateDraftRequest request) {
        PostDraft draft = new PostDraft();
        draft.id = UUID.randomUUID().toString();
        draft.postId = request.postId;
        draft.categoryId = request.categoryId;
        draft.version = request.version;
        draft.path = URLs.normalize(request.path);
        draft.templatePath = request.templatePath;
        draft.title = request.title;
        draft.description = request.description;
        draft.tags = request.tags == null || request.tags.isEmpty() ? null : Joiner.on(';').join(request.tags);
        draft.keywords = request.keywords == null || request.keywords.isEmpty() ? null : Joiner.on(';').join(request.keywords);
        draft.fields = request.fields == null ? null : JSON.toJSON(request.fields);
        draft.imageURL = request.imageURL;
        draft.imageURLs = Joiner.on(";").join(Markdown.imageURLs(request.content));
        draft.userId = request.userId;
        draft.updatedBy = request.requestBy;
        draft.updatedTime = OffsetDateTime.now();
        draft.createdBy = request.requestBy;
        draft.createdTime = OffsetDateTime.now();
        draft.status = PostStatus.DRAFT;
        draft.topFixed = request.topFixed != null && request.topFixed;
        draft.content = request.content;
        repository.insert(draft);
        return draft;
    }


    @Transactional
    public void create(PostDraft postDraft) {
        repository.insert(postDraft);
    }

    @Transactional
    public void delete(String id) {
        repository.delete(id);
    }

    @Transactional
    public void deleteByPostId(String postId) {
        repository.execute("DELETE FROM PostDraft t WHERE t.postId=?0", postId);
    }
}
