package app.jweb.post.service;

import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.post.api.tag.BatchCreatePostTagRequest;
import app.jweb.post.api.tag.BatchDeletePostTagRequest;
import app.jweb.post.api.tag.BatchGetTagRequest;
import app.jweb.post.api.tag.CreatePostTagRequest;
import app.jweb.post.api.tag.PostTagQuery;
import app.jweb.post.api.tag.PostTagStatus;
import app.jweb.post.api.tag.PostTagTreeQuery;
import app.jweb.post.api.tag.PostTagView;
import app.jweb.post.api.tag.UpdatePostTagRequest;
import app.jweb.post.domain.PostTag;
import app.jweb.post.util.URLs;
import app.jweb.util.JSON;
import app.jweb.util.collection.QueryResponse;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class PostTagService {
    @Inject
    Repository<PostTag> repository;

    public PostTag get(String id) {
        return repository.get(id);
    }

    public List<PostTag> batchGet(BatchGetTagRequest request) {
        if (request.tags == null || request.tags.isEmpty()) {
            return ImmutableList.of();
        }
        Query<PostTag> tagQuery = repository.query("SELECT t FROM PostTag t WHERE 1=1 AND t.name in(");
        for (int i = 0; i < request.tags.size(); i++) {
            if (i != 0) {
                tagQuery.append(",");
            }
            tagQuery.append("?" + i, URLs.segment(request.tags.get(i)));
        }
        tagQuery.append(")");
        return tagQuery.find();
    }

    public Optional<PostTag> findByDisplayName(String displayName) {
        return repository.query("SELECT t FROM PostTag t WHERE t.name=?0", URLs.segment(displayName)).findOne();
    }

    public QueryResponse<PostTag> find(PostTagQuery query) {
        Query<PostTag> tagQuery = repository.query("SELECT t FROM PostTag t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(query.displayName)) {
            tagQuery.append("AND t.displayName LIKE ?" + index++, "%" + URLs.segment(query.displayName) + "%");
        }
        if (!Strings.isNullOrEmpty(query.type)) {
            tagQuery.append("AND t.type=?" + index++, query.type);
        }
        if (query.level != null) {
            tagQuery.append("AND t.level=?" + index++, query.level);
        }
        if (query.status != null) {
            tagQuery.append("AND t.status=?" + index++, query.status);
        }
        if (query.parentId != null) {
            tagQuery.append("AND t.parentId=?" + index++, query.parentId);
        }
        if (query.tags != null && !query.tags.isEmpty()) {
            tagQuery.append("AND (");
            for (int i = 0; i < query.tags.size(); i++) {
                if (i != 0) {
                    tagQuery.append("OR");
                }
                tagQuery.append("t.tags LIKE ?" + index++, "%" + query.tags.get(i) + "%");
            }
            tagQuery.append(")");
        }
        if (query.sortingField != null) {
            tagQuery.sort("t." + query.sortingField, query.desc);
        }
        tagQuery.limit(query.page, query.limit);
        return tagQuery.findAll();
    }


    public List<PostTag> find(PostTagTreeQuery categoryTreeQuery) {
        Query<PostTag> query = repository.query("SELECT t FROM PostTag t WHERE 1=1");
        int index = 0;
        if (categoryTreeQuery.status != null) {
            query.append("AND t.status=?" + index++, categoryTreeQuery.status);
        }
        if (categoryTreeQuery.level != null) {
            query.append("AND t.level<=?" + index++, categoryTreeQuery.level);
        }
        if (categoryTreeQuery.parentId != null) {
            PostTag parent = get(categoryTreeQuery.parentId);
            query.append("AND t.parentIds LIKE ?" + index, parent.parentIds + '%');
        }
        return query.find();
    }

    public List<PostTag> children(String id) {
        Query<PostTag> query = repository.query("SELECT t FROM PostTag t WHERE t.parentId=?0 AND t.status=?1", id, PostTagStatus.ACTIVE);
        query.sort("t.displayName", false);
        return query.find();
    }

    @Transactional
    public PostTag create(CreatePostTagRequest request) {
        Optional<PostTag> postTagOptional = findByDisplayName(request.displayName);
        if (postTagOptional.isPresent()) {
            PostTag postTag = postTagOptional.get();
            if (request.parentId != null) {
                postTag.parentId = request.parentId;
                PostTag parent = get(request.parentId);
                postTag.parentIds = parent.parentIds == null ? parent.id : parent.parentIds + ';' + parent.id;
                postTag.level = parent.level + 1;
            } else {
                postTag.level = 1;
            }
            postTag.tags = request.tags == null ? postTag.tags : Joiner.on(";").join(request.tags);
            if (request.tags != null) {
                tag(request.tags, request.requestBy);
            }
            if (request.fields != null) {
                postTag.fields = JSON.toJSON(request.fields);
            }
            postTag.alias = request.alias;
            postTag.displayName = request.displayName;
            postTag.name = request.name == null ? URLs.segment(request.displayName) : request.name;
            postTag.displayOrder = request.displayOrder;
            postTag.description = request.description;
            postTag.imageURL = request.imageURL;
            postTag.totalTagged = 0;
            postTag.type = request.type;
            postTag.updatedTime = OffsetDateTime.now();
            postTag.updatedBy = request.requestBy;
            repository.update(postTag.id, postTag);
            return postTag;
        }
        PostTag postTag = new PostTag();
        postTag.parentId = request.parentId;
        if (request.parentId != null) {
            PostTag parent = get(request.parentId);
            postTag.parentIds = parent.parentIds == null ? parent.id : parent.parentIds + ';' + parent.id;
            postTag.level = parent.level + 1;
        } else {
            postTag.level = 1;
        }
        postTag.id = UUID.randomUUID().toString();
        postTag.tags = request.tags == null ? null : Joiner.on(";").join(request.tags);
        if (request.tags != null) {
            tag(request.tags, request.requestBy);
        }
        postTag.fields = request.fields == null ? null : JSON.toJSON(request.fields);
        postTag.alias = request.alias;
        postTag.displayName = request.displayName;
        postTag.name = request.name == null ? URLs.segment(request.displayName) : request.name;
        postTag.displayOrder = request.displayOrder;
        postTag.description = request.description;
        postTag.imageURL = request.imageURL;
        postTag.totalTagged = 0;
        postTag.type = request.type;
        postTag.status = PostTagStatus.ACTIVE;
        postTag.createdTime = OffsetDateTime.now();
        postTag.updatedTime = OffsetDateTime.now();
        postTag.createdBy = request.requestBy;
        postTag.updatedBy = request.requestBy;

        repository.insert(postTag);
        return postTag;
    }

    @Transactional
    public PostTag create(PostTag postTag) {
        repository.insert(postTag);
        return postTag;
    }

    @Transactional
    public PostTag update(String id, UpdatePostTagRequest request) {
        PostTag postTag = get(id);
        postTag.parentId = request.parentId;
        if (request.parentId != null) {
            PostTag parent = get(request.parentId);
            postTag.parentIds = parent.parentIds == null ? parent.id : parent.parentIds + ';' + parent.id;
            postTag.level = parent.level + 1;
        } else {
            postTag.level = 1;
        }
        postTag.tags = request.tags == null ? null : Joiner.on(";").join(request.tags);
        if (request.tags != null) {
            tag(request.tags, request.requestBy);
        }
        if (request.fields != null) {
            postTag.fields = JSON.toJSON(request.fields);
        }
        if (request.alias != null) {
            postTag.alias = request.alias;
        }
        if (request.displayName != null) {
            postTag.displayName = request.displayName;
        }
        postTag.name = Objects.requireNonNullElseGet(request.name, () -> URLs.segment(postTag.displayName));
        if (request.displayOrder != null) {
            postTag.displayOrder = request.displayOrder;
        }
        if (request.description != null) {
            postTag.description = request.description;
        }
        if (request.imageURL != null) {
            postTag.imageURL = request.imageURL;
        }
        if (request.totalTagged != null) {
            postTag.totalTagged = request.totalTagged;
        }
        if (request.type != null) {
            postTag.type = request.type;
        }
        if (request.status != null) {
            postTag.status = request.status;
        }
        postTag.updatedTime = OffsetDateTime.now();
        postTag.updatedBy = request.requestBy;
        repository.update(id, postTag);
        return postTag;
    }

    @Transactional
    public void batchDelete(BatchDeletePostTagRequest request) {
        List<PostTag> postTags = repository.batchGet(request.ids);
        List<String> toDelete = Lists.newArrayList();
        for (PostTag postTag : postTags) {
            if (postTag.status == PostTagStatus.INACTIVE) {
                toDelete.add(postTag.id);
            } else {
                postTag.status = PostTagStatus.INACTIVE;
                postTag.updatedBy = request.requestBy;
                postTag.updatedTime = OffsetDateTime.now();
                repository.update(postTag.id, postTag);
            }
        }

        repository.batchDelete(toDelete);
    }

    @Transactional
    public void tag(List<String> tags, String requestBy) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
        List<String> toIncreaseTagged = Lists.newArrayList();
        for (String tag : tags) {
            Optional<PostTag> postTagOptional = findByDisplayName(tag);
            if (postTagOptional.isPresent()) {
                toIncreaseTagged.add(postTagOptional.get().id);
            } else {
                PostTag postTag = new PostTag();
                postTag.id = UUID.randomUUID().toString();
                postTag.parentId = null;
                postTag.level = 1;
                postTag.displayName = tag;
                postTag.name = URLs.segment(tag);
                postTag.fields = null;
                postTag.displayOrder = 0;
                postTag.totalTagged = 1;
                postTag.status = PostTagStatus.ACTIVE;
                postTag.updatedTime = OffsetDateTime.now();
                postTag.updatedBy = requestBy;
                postTag.createdTime = OffsetDateTime.now();
                postTag.createdBy = requestBy;
                create(postTag);
            }
        }
        increaseTotalTagged(toIncreaseTagged, 1);
    }

    /*private List<PostTag> findByDisplayNames(List<String> tags) {
        Query<PostTag> query = repository.query("SELECT t FROM PostTag t WHERE t.name in(");
        for (int i = 0; i < tags.size(); i++) {
            if (i != 0) {
                query.append(",");
            }
            query.append("\'" + URLs.segment(tags.get(i)) + "\'");
        }
        query.append(")");
        return query.find();
    }*/

    @Transactional
    private void increaseTotalTagged(List<String> ids, int count) {
        if (ids.isEmpty()) {
            return;
        }
        StringBuilder b = new StringBuilder(128);
        b.append("UPDATE PostTag t SET t.totalTagged = t.totalTagged+")
            .append(count)
            .append(" WHERE t.id in(");
        for (int i = 0; i < ids.size(); i++) {
            if (i != 0) {
                b.append(',');
            }
            b.append('\'').append(ids.get(i)).append('\'');
        }
        b.append(')');
        repository.execute(b.toString());
    }

    @Transactional
    public void untag(List<String> tags, String updatedBy) {
        if (tags == null || tags.isEmpty()) {
            return;
        }
        StringBuilder b = new StringBuilder(128);
        b.append("UPDATE PostTag SET t.totalTagged = t.totalTagged-1 WHERE t.displayName in(");
        for (int i = 0; i < tags.size(); i++) {
            if (i != 0) {
                b.append(',');
            }
            b.append('\'').append(tags.get(i).toLowerCase(Locale.getDefault())).append('\'');
        }
        b.append(')');
        repository.execute(b.toString());
    }

    @Transactional
    public List<PostTag> batchCreate(BatchCreatePostTagRequest request) {
        List<PostTag> postTags = Lists.newArrayList();
        List<PostTag> toCreate = Lists.newArrayList();
        for (PostTagView tag : request.tags) {
            Optional<PostTag> tagOptional = findByDisplayName(URLs.segment(tag.displayName));
            if (tagOptional.isPresent()) {
                PostTag postTag = tagOptional.get();
                if (tag.tags != null) {
                    postTag.tags = Joiner.on(";").join(tag.tags);
                    tag(tag.tags, request.requestBy);
                }
                if (tag.fields != null) {
                    postTag.fields = JSON.toJSON(tag.fields);
                }
                postTag.alias = tag.alias;
                postTag.displayName = tag.displayName;
                if (tag.fields != null) {
                    postTag.fields = JSON.toJSON(tag.fields);
                }
                if (tag.alias != null) {
                    postTag.alias = tag.alias;
                }
                if (tag.displayName != null) {
                    postTag.displayName = tag.displayName;
                }
                postTag.name = tag.name == null ? URLs.segment(tag.displayName) : tag.name;
                if (tag.displayOrder != null) {
                    postTag.displayOrder = tag.displayOrder;
                }
                if (tag.description != null) {
                    postTag.description = tag.description;
                }
                if (tag.imageURL != null) {
                    postTag.imageURL = tag.imageURL;
                }
                postTag.updatedTime = OffsetDateTime.now();
                postTag.updatedBy = request.requestBy;
                repository.update(postTag.id, postTag);
                postTags.add(postTag);
            } else {
                PostTag postTag = new PostTag();
                postTag.id = UUID.randomUUID().toString();
                postTag.level = 1;
                postTag.parentId = null;
                postTag.tags = tag.tags == null ? null : Joiner.on(";").join(tag.tags);
                if (tag.tags != null) {
                    tag(tag.tags, request.requestBy);
                }
                if (tag.fields != null) {
                    postTag.fields = JSON.toJSON(tag.fields);
                }
                postTag.alias = tag.alias;
                postTag.type = tag.type;
                postTag.displayName = tag.displayName;
                postTag.name = tag.name == null ? URLs.segment(tag.displayName) : tag.name;
                postTag.displayOrder = tag.displayOrder;
                postTag.description = tag.description;
                postTag.imageURL = tag.imageURL;
                postTag.totalTagged = 0;
                postTag.status = PostTagStatus.ACTIVE;
                postTag.createdTime = OffsetDateTime.now();
                postTag.updatedTime = OffsetDateTime.now();
                postTag.createdBy = request.requestBy;
                postTag.updatedBy = request.requestBy;

                toCreate.add(postTag);
                postTags.add(postTag);
            }
            repository.batchInsert(toCreate);
        }
        return postTags;
    }

}
