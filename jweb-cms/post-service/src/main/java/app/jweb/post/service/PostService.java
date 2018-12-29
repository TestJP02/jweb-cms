package app.jweb.post.service;


import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.message.MessagePublisher;
import app.jweb.post.api.post.CreatePostRequest;
import app.jweb.post.api.post.LatestPostQuery;
import app.jweb.post.api.post.PostCreatedMessage;
import app.jweb.post.api.post.PostDeletedMessage;
import app.jweb.post.api.post.PostQuery;
import app.jweb.post.api.post.PostRelatedQuery;
import app.jweb.post.api.post.PostStatus;
import app.jweb.post.api.post.PostUpdatedMessage;
import app.jweb.post.api.post.TopFixedPostQuery;
import app.jweb.post.api.post.UpdatePostRequest;
import app.jweb.post.domain.Post;
import app.jweb.post.domain.PostDraft;
import app.jweb.post.util.Markdown;
import app.jweb.post.util.URLs;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import app.jweb.util.exception.Exceptions;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PostService {
    @Inject
    Repository<Post> repository;
    @Inject
    PostDraftService postDraftService;
    @Inject
    PostContentService postContentService;
    @Inject
    MessagePublisher<PostDeletedMessage> postDeletePublisher;
    @Inject
    MessagePublisher<PostCreatedMessage> postCreatedMessagePublisher;
    @Inject
    MessagePublisher<PostUpdatedMessage> postUpdatedMessagePublisher;
    @Inject
    PostCategoryService postCategoryService;

    public Post get(String id) {
        return repository.get(id);
    }

    public List<Post> batchGet(List<String> ids) {
        return repository.batchGet(ids);
    }

    public Optional<Post> find(String path, String language) {
        return repository.query("SELECT t from Post t WHERE t.path=?0 AND t.language=?1 AND t.status=?2", path, language, PostStatus.ACTIVE).findOne();
    }

    public QueryResponse<Post> find(PostQuery pageQuery) {
        int index = 0;
        Query<Post> query = repository.query("SELECT t from Post t WHERE 1=1");
        if (!Strings.isNullOrEmpty(pageQuery.query)) {
            query.append("AND (t.title LIKE ?" + index++ + " OR t.path LIKE ?" + index++ + ")", "%" + pageQuery.query + "%", "%" + pageQuery.query + "%");
        }
        if (!Strings.isNullOrEmpty(pageQuery.categoryId)) {
            List<String> ids = postCategoryService.childrenIds(pageQuery.categoryId);
            query.append("AND t.categoryId IN (");

            for (int i = 0; i < ids.size(); i++) {
                String categoryId = ids.get(i);
                if (i != 0) {
                    query.append(",");
                }
                query.append("?" + index++, categoryId);
            }
            query.append(")");
        }

        if (pageQuery.userId != null) {
            query.append("AND t.userId=?" + index++, pageQuery.userId);
        }

        if (pageQuery.status != null) {
            query.append("AND t.status=?" + index++, pageQuery.status);
        }

        if (pageQuery.tags != null && !pageQuery.tags.isEmpty()) {
            query.append("AND (");
            for (int i = 0; i < pageQuery.tags.size(); i++) {
                if (i != 0) {
                    query.append("OR");
                }
                query.append("t.tags LIKE ?" + index++, "%" + pageQuery.tags.get(i) + "%");
            }
            query.append(")");
        }

        if (pageQuery.startTime != null) {
            query.append("AND t.createdTime>=?" + index++, pageQuery.startTime);
        }

        if (pageQuery.endTime != null) {
            query.append("AND t.createdTime<?" + index, pageQuery.endTime);
        }

        query.limit(pageQuery.page, pageQuery.limit);
        if (!Strings.isNullOrEmpty(pageQuery.sortingField)) {
            query.sort("t." + pageQuery.sortingField, pageQuery.desc);
        }
        return query.findAll();
    }

    public Optional<Post> findById(String id) {
        return repository.query("SELECT t from Post t WHERE t.id=?0", id).findOne();
    }

    public Optional<Post> findByPath(String path) {
        return repository.query("SELECT t from Post t WHERE t.path=?0 AND t.status=?1", path, PostStatus.ACTIVE).findOne();
    }


    public Optional<Post> findNext(String id) {
        Optional<Post> post = findById(id);
        if (!post.isPresent()) {
            return Optional.empty();
        }
        return repository.query("SELECT t from Post t WHERE t.updatedTime>?0 AND t.categoryId!=null", post.get().updatedTime).sort("t.updatedTime", false).findOne();
    }

    public Optional<Post> findPrev(String id) {
        Optional<Post> post = findById(id);
        if (!post.isPresent()) {
            return Optional.empty();
        }
        return repository.query("SELECT t from Post t WHERE t.updatedTime<?0 AND t.categoryId!=null", post.get().updatedTime).sort("t.updatedTime", true).findOne();
    }

    public List<Post> findRelated(PostRelatedQuery pageRelatedQuery) {
        Optional<Post> pageOptional = findById(pageRelatedQuery.id);
        if (!pageOptional.isPresent()) {
            return ImmutableList.of();
        }
        Post post = pageOptional.get();
        int index = 0;
        Query<Post> query = repository.query("SELECT t from Post t WHERE t.id!=?" + index++, post.id);

        if (post.categoryId != null) {
            query.append("AND t.categoryId=?" + index++, post.categoryId);
        }
        if (!Strings.isNullOrEmpty(post.tags)) {
            List<String> tags = Splitter.on(";").splitToList(post.tags);
            query.append("AND (");
            for (int i = 0; i < tags.size(); i++) {
                if (i != 0) {
                    query.append("OR");
                }
                query.append("t.tags LIKE ?" + index++, "%" + tags.get(i) + "%");
            }
            query.append(")");
        }
        return query.limit(1, pageRelatedQuery.limit).find();
    }

    public QueryResponse<Post> topFixed(TopFixedPostQuery query) {
        return repository.query("SELECT t FROM Post t WHERE t.topFixed=?0 AND t.status=?1", true, PostStatus.ACTIVE).sort("t.updatedTime", true).limit(query.page, query.limit).findAll();
    }

    @Transactional
    public void delete(String id, String requestBy) {
        Post post = repository.get(id);
        if (post.status == PostStatus.INACTIVE) {
            postContentService.delete(post.id);
            repository.delete(post.id);
            postDraftService.deleteByPostId(post.id);
            post.status = PostStatus.DELETED;

            notifyPostDelete(post);
        } else {
            post.status = PostStatus.INACTIVE;
            post.updatedBy = requestBy;
            post.updatedTime = OffsetDateTime.now();
            repository.update(id, post);
        }
    }

    private void notifyPostCreated(Post post) {
        PostCreatedMessage message = new PostCreatedMessage();
        message.id = post.id;
        message.userId = post.userId;
        message.categoryId = post.categoryId;
        message.tags = post.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(post.tags);
        message.keywords = post.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(post.keywords);
        message.fields = post.fields == null ? ImmutableMap.of() : JSON.fromJSON(post.fields, Map.class);
        message.imageURLs = post.imageURLs == null ? ImmutableList.of() : Splitter.on(";").splitToList(post.imageURLs);
        message.path = post.path;
        message.templatePath = post.templatePath;
        message.title = post.title;
        message.description = post.description;
        message.imageURL = post.imageURL;
        message.version = post.version;
        message.status = post.status;
        message.topFixed = post.topFixed;
        message.createdTime = post.createdTime;
        message.createdBy = post.createdBy;
        message.updatedTime = post.updatedTime;
        message.updatedBy = post.updatedBy;
        postCreatedMessagePublisher.publish(message);
    }

    private void notifyPostUpdated(Post post) {
        PostUpdatedMessage message = new PostUpdatedMessage();
        message.id = post.id;
        message.userId = post.userId;
        message.categoryId = post.categoryId;
        message.tags = post.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(post.tags);
        message.keywords = post.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(post.keywords);
        message.fields = post.fields == null ? ImmutableMap.of() : JSON.fromJSON(post.fields, Map.class);
        message.imageURLs = post.imageURLs == null ? ImmutableList.of() : Splitter.on(";").splitToList(post.imageURLs);
        message.path = post.path;
        message.templatePath = post.templatePath;
        message.title = post.title;
        message.description = post.description;
        message.imageURL = post.imageURL;
        message.version = post.version;
        message.status = post.status;
        message.topFixed = post.topFixed;
        message.createdTime = post.createdTime;
        message.createdBy = post.createdBy;
        message.updatedTime = post.updatedTime;
        message.updatedBy = post.updatedBy;
        postUpdatedMessagePublisher.publish(message);
    }


    private void notifyPostDelete(Post post) {
        PostDeletedMessage message = new PostDeletedMessage();
        message.id = post.id;
        message.userId = post.userId;
        message.categoryId = post.categoryId;
        message.tags = post.tags == null ? ImmutableList.of() : Splitter.on(';').splitToList(post.tags);
        message.keywords = post.keywords == null ? ImmutableList.of() : Splitter.on(";").splitToList(post.keywords);
        message.fields = post.fields == null ? ImmutableMap.of() : JSON.fromJSON(post.fields, Map.class);
        message.imageURLs = post.imageURLs == null ? ImmutableList.of() : Splitter.on(";").splitToList(post.imageURLs);
        message.path = post.path;
        message.templatePath = post.templatePath;
        message.title = post.title;
        message.description = post.description;
        message.imageURL = post.imageURL;
        message.version = post.version;
        message.status = post.status;
        message.topFixed = post.topFixed;
        message.createdTime = post.createdTime;
        message.createdBy = post.createdBy;
        message.updatedTime = post.updatedTime;
        message.updatedBy = post.updatedBy;
        postDeletePublisher.publish(message);
    }

    @Transactional
    public Post publish(String id, String requestBy) {
        PostDraft draft = postDraftService.get(id);
        if (!Strings.isNullOrEmpty(draft.postId)) {
            Post post = repository.get(draft.postId);
            post.path = draft.path;
            post.templatePath = draft.templatePath;
            post.version = draft.version == null ? Integer.valueOf(1) : draft.version;
            post.categoryId = draft.categoryId;
            post.userId = draft.userId;
            post.title = draft.title;
            post.description = draft.description;
            post.tags = draft.tags;
            post.keywords = draft.keywords;
            post.fields = draft.fields;
            post.imageURL = draft.imageURL;
            post.imageURLs = draft.imageURLs;
            post.status = PostStatus.ACTIVE;
            post.topFixed = draft.topFixed;
            post.updatedTime = OffsetDateTime.now();
            post.updatedBy = requestBy;
            postContentService.update(post.id, draft.content, requestBy);
            repository.update(post.id, post);
            postDraftService.delete(id);
            notifyPostCreated(post);
            return post;
        } else {
            Post post = new Post();
            post.id = draft.id;
            post.userId = draft.userId;
            post.path = draft.path;
            post.templatePath = draft.templatePath;
            post.fields = draft.fields;
            post.version = 1;
            post.categoryId = draft.categoryId;
            post.title = draft.title;
            post.description = draft.description;
            post.tags = draft.tags;
            post.keywords = draft.keywords;
            post.imageURL = draft.imageURL;
            post.imageURLs = draft.imageURLs;
            post.status = PostStatus.ACTIVE;
            post.topFixed = draft.topFixed;
            post.createdTime = OffsetDateTime.now();
            post.createdBy = requestBy;
            post.updatedTime = OffsetDateTime.now();
            post.updatedBy = requestBy;
            postContentService.create(post.id, draft.content, requestBy);
            repository.insert(post);
            postDraftService.delete(id);
            notifyPostCreated(post);
            return post;
        }
    }

    @Transactional
    public void revert(String id, String requestBy) {
        Post post = get(id);
        if (post.status != PostStatus.INACTIVE) {
            throw Exceptions.badRequestException("status", "post is not inactive");
        }
        post.status = PostStatus.ACTIVE;
        post.updatedBy = requestBy;
        post.updatedTime = OffsetDateTime.now();
        repository.update(post.id, post);
        notifyPostUpdated(post);
    }


    public List<Post> latest(LatestPostQuery query) {
        return repository.query("SELECT t FROM Post t WHERE t.status=?0", PostStatus.ACTIVE).sort("t.updatedTime", true).limit(1, query.limit).find();
    }

    @Transactional
    public Post create(CreatePostRequest request) {
        Optional<Post> postOptional = findByPath(request.path);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.path = URLs.normalize(request.path);
            post.templatePath = request.templatePath;
            post.version = post.version + 1;
            post.categoryId = request.categoryId;
            post.userId = request.userId;
            post.title = request.title;
            post.description = request.description;
            post.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
            post.keywords = request.keywords == null ? null : Joiner.on(';').join(request.keywords);
            post.fields = request.fields == null ? null : JSON.toJSON(request.fields);
            post.imageURL = request.imageURL;
            post.imageURLs = Joiner.on(";").join(Markdown.imageURLs(request.content));
            post.status = PostStatus.ACTIVE;
            post.topFixed = request.topFixed != null && request.topFixed;
            post.updatedTime = OffsetDateTime.now();
            post.updatedBy = request.requestBy;
            postContentService.update(post.id, request.content, request.requestBy);
            repository.update(post.id, post);
            notifyPostCreated(post);
            return post;
        } else {
            Post post = new Post();
            post.id = UUID.randomUUID().toString();
            post.userId = request.userId;
            post.path = URLs.normalize(request.path);
            post.templatePath = request.templatePath;
            post.tags = request.tags == null ? null : Joiner.on(';').join(request.tags);
            post.keywords = request.keywords == null ? null : Joiner.on(';').join(request.keywords);
            post.fields = request.fields == null ? null : JSON.toJSON(request.fields);
            post.version = 1;
            post.categoryId = request.categoryId;
            post.title = request.title;
            post.description = request.description;
            post.imageURL = request.imageURL;
            post.imageURLs = Joiner.on(";").join(Markdown.imageURLs(request.content));
            post.status = PostStatus.ACTIVE;
            post.topFixed = request.topFixed != null && request.topFixed;
            post.createdTime = OffsetDateTime.now();
            post.createdBy = request.requestBy;
            post.updatedTime = OffsetDateTime.now();
            post.updatedBy = request.requestBy;
            postContentService.create(post.id, request.content, request.requestBy);
            repository.insert(post);
            notifyPostUpdated(post);
            return post;
        }
    }

    @Transactional
    public Post update(String id, UpdatePostRequest request) {
        Post post = get(id);
        if (request.userId != null) {
            post.userId = request.userId;
        }
        if (request.path != null) {
            post.path = request.path;
        }
        if (request.templatePath != null) {
            post.templatePath = request.templatePath;
        }
        if (request.categoryId != null) {
            post.categoryId = request.categoryId;
        }
        if (request.keywords != null) {
            post.keywords = Joiner.on(";").join(request.keywords);
        }
        if (request.title != null) {
            post.title = request.title;
        }
        if (request.description != null) {
            post.description = request.description;
        }
        if (request.tags != null) {
            post.tags = Joiner.on(";").join(request.tags);
        }
        if (request.imageURL != null) {
            post.imageURL = request.imageURL;
        }
        if (request.topFixed != null) {
            post.topFixed = request.topFixed;
        }
        if (request.status != null) {
            post.status = request.status;
        }
        if (request.fields != null) {
            post.fields = JSON.toJSON(request.fields);
        }
        post.updatedBy = request.requestBy;
        post.updatedTime = OffsetDateTime.now();
        repository.update(id, post);
        notifyPostUpdated(post);
        return post;
    }
}