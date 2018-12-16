package app.jweb.comment.service;

import app.jweb.comment.api.comment.CommentNodeResponse;
import app.jweb.comment.api.comment.CommentQuery;
import app.jweb.comment.api.comment.CommentResponse;
import app.jweb.comment.api.comment.CommentStatus;
import app.jweb.comment.api.comment.CommentTreeQuery;
import app.jweb.comment.api.comment.CreateCommentRequest;
import app.jweb.comment.api.comment.UpdateCommentRequest;
import app.jweb.comment.domain.PageComment;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import app.jweb.database.Query;
import app.jweb.database.Repository;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageCommentService {
    @Inject
    Repository<PageComment> repository;


    public PageComment get(String id) {
        return repository.get(id);
    }


    public List<PageComment> batchGet(List<String> ids) {
        return repository.batchGet(ids);
    }

    public QueryResponse<CommentNodeResponse> tree(CommentTreeQuery query) {
        int queryIndex = 0;
        Query<PageComment> firstLevelQuery = repository.query(String.format("SELECT t FROM PageComment t WHERE t.pageId=?%d AND t.parentId IS NULL", queryIndex), query.pageId);
        queryIndex++;
        if (query.status != null) {
            firstLevelQuery.append(String.format("AND t.status=?%d", queryIndex), query.status);
            queryIndex++;
        }
        QueryResponse<PageComment> queryResponse = firstLevelQuery.limit(query.page, query.limit)
            .sort("t.createdTime", true).findAll();
        List<String> ids = queryResponse.items.stream().map(item -> item.id).collect(Collectors.toList());
        queryIndex = 0;
        Query<PageComment> replyQuery = repository.query(String.format("SELECT t FROM PageComment t WHERE t.pageId=?%d AND t.parentId is not null", queryIndex), query.pageId);
        queryIndex++;
        if (query.status != null) {
            replyQuery.append(String.format("AND t.status=?%d", queryIndex), query.status);
            queryIndex++;
        }
        if (!ids.isEmpty()) {
            int index = 0;
            replyQuery.append("AND t.firstParentId IN (");
            int inLimit = index + ids.size() - 1;
            for (String categoryId : ids) {
                replyQuery.append("?" + queryIndex, categoryId);
                queryIndex++;
                if (index < inLimit) replyQuery.append(",");
                index++;
            }
            replyQuery.append(")");
        }
        List<PageComment> replies = replyQuery.sort("t.createdTime").find();
        return tree(queryResponse, replies);
    }

    private QueryResponse<CommentNodeResponse> tree(QueryResponse<PageComment> queryResponse, List<PageComment> replies) {
        Map<String, CommentNodeResponse> index = Maps.newHashMap();
        for (PageComment comment : queryResponse) {
            CommentNodeResponse nodeResponse = new CommentNodeResponse();
            nodeResponse.comment = response(comment);
            nodeResponse.children = children();
            index.put(comment.id, nodeResponse);
        }

        for (PageComment reply : replies) {
            CommentNodeResponse replyResponse = new CommentNodeResponse();
            replyResponse.comment = response(reply);
            replyResponse.children = children();

            index.put(reply.id, replyResponse);
            CommentNodeResponse parent = index.get(reply.parentId);
            if (parent != null) {
                parent.children.items.add(0, replyResponse);
            }
        }
        return queryResponse.map(comment -> index.get(comment.id));
    }

    private QueryResponse<CommentNodeResponse> children() {
        QueryResponse<CommentNodeResponse> commentNodeResponses = new QueryResponse<>();
        commentNodeResponses.limit = 10;
        commentNodeResponses.page = 1;
        commentNodeResponses.total = 0L;
        commentNodeResponses.items = Lists.newArrayList();
        return commentNodeResponses;
    }

    public QueryResponse<PageComment> find(CommentQuery commentQuery) {
        Query<PageComment> query = repository.query("SELECT t FROM PageComment t WHERE 1=1");
        int index = 0;
        if (commentQuery.status != null) {
            query.append(String.format("AND t.status=?%d", index), commentQuery.status);
            index++;
        }
        if (!Strings.isNullOrEmpty(commentQuery.pageId)) {
            query.append(String.format("AND t.pageId=?%d", index), commentQuery.pageId);
            index++;
        }
        if (!Strings.isNullOrEmpty(commentQuery.userId)) {
            query.append(String.format("AND t.userId=?%d", index), commentQuery.userId);
            index++;
        }
        if (!Strings.isNullOrEmpty(commentQuery.title)) {
            query.append(String.format("AND t.title LIKE ?%d", index), '%' + commentQuery.title + '%');
            index++;
        }

        if (!Strings.isNullOrEmpty(commentQuery.parentId)) {
            query.append(String.format("AND t.parentId=?%d", index), commentQuery.parentId);
            index++;
        }

        if (!Strings.isNullOrEmpty(commentQuery.firstParentId)) {
            query.append(String.format("AND t.firstParentId=?%d", index), commentQuery.firstParentId);
            index++;
        } else {
            query.append("AND t.parentId IS NULL");
        }

        query.limit(commentQuery.page, commentQuery.limit);
        if (!Strings.isNullOrEmpty(commentQuery.sortingField)) {
            query.sort("t." + commentQuery.sortingField, commentQuery.desc);
        }
        return query.findAll();
    }

    @Transactional
    public PageComment create(CreateCommentRequest request) {
        PageComment comment = new PageComment();
        comment.id = UUID.randomUUID().toString();
        comment.pageId = request.pageId;
        comment.userId = request.userId;
        comment.ip = request.ip;
        if (request.parentId == null) {
            comment.firstParentId = comment.id;
        } else {
            comment.parentId = request.parentId;
            PageComment parent = get(comment.parentId);
            comment.firstParentId = parent.firstParentId;
        }
        comment.ip = request.ip;
        if (request.parentId != null) {
            increaseReplies(comment.parentId, 1);
        }
        comment.content = request.content;
        comment.totalReplies = 0;
        comment.totalVoteDown = 0;
        comment.totalVoteUp = 0;
        comment.status = CommentStatus.ACTIVE;
        comment.createdBy = request.requestBy;
        comment.createdTime = OffsetDateTime.now();
        comment.updatedBy = request.requestBy;
        comment.updatedTime = OffsetDateTime.now();
        return repository.insert(comment);
    }

    @Transactional
    public PageComment update(String id, UpdateCommentRequest request) {
        PageComment comment = get(id);
        if (request.content != null) {
            comment.content = request.content;
        }
        if (request.status != null) {
            comment.status = request.status;
        }
        comment.updatedBy = request.requestBy;
        comment.updatedTime = OffsetDateTime.now();
        return repository.update(id, comment);
    }

    @Transactional
    public boolean delete(String id, String requestBy) {
        PageComment comment = get(id);
        if (comment.status.equals(CommentStatus.INACTIVE) && comment.parentId != null) {
            decreaseReplies(comment.parentId, 1);
            return repository.delete(id);
        }
        comment.status = CommentStatus.INACTIVE;
        comment.updatedBy = requestBy;
        comment.updatedTime = OffsetDateTime.now();
        repository.update(id, comment);
        return true;
    }

    @Transactional
    public PageComment increaseVoteUp(String id, int count) {
        repository.execute("UPDATE PageComment t SET t.totalVoteUp=t.totalVoteUp+?0 WHERE t.id=?1", count, id);
        return get(id);
    }

    @Transactional
    public PageComment decreaseVoteDown(String id, int count) {
        repository.execute("UPDATE PageComment t SET t.totalVoteUp=t.totalVoteUp-?0 WHERE t.id=?1", count, id);
        return get(id);
    }

    @Transactional
    public void increaseReplies(String id, int count) {
        repository.execute("UPDATE PageComment t SET t.totalReplies=t.totalReplies+?0 WHERE t.id=?1", count, id);
    }

    @Transactional
    public void decreaseReplies(String id, int count) {
        repository.execute("UPDATE PageComment t SET t.totalReplies=totalReplies-?0 WHERE t.id=?1", count, id);
    }

    @Transactional
    public void inactiveByTopic(String pageId, String requestBy) {
        String sql = "UPDATE PageComment t SET t.status=?0,t.updatedTime=?1,t.updatedBy=?2 WHERE t.pageId=?3 AND t.status=?4";
        repository.execute(sql, CommentStatus.INACTIVE, OffsetDateTime.now(), requestBy, pageId, CommentStatus.ACTIVE);
    }

    @Transactional
    public void inactiveByTopics(List<String> pageIds, String requestBy) {
        StringBuilder sql = new StringBuilder("UPDATE PageComment t SET t.status=?0,t.updatedTime=?1,t.updatedBy=?2 WHERE t.status=?3 AND t.pageId IN (");
        int index = 0;
        int limit = index + pageIds.size() - 1;
        for (String pageId : pageIds) {
            sql.append('\'');
            sql.append(pageId);
            sql.append('\'');
            if (index < limit) sql.append(',');
            index++;
        }
        sql.append(')');
        repository.execute(sql.toString(), CommentStatus.INACTIVE, OffsetDateTime.now(), requestBy, CommentStatus.ACTIVE);
    }

    @Transactional
    public void deleteByPageId(String pageId) {
        repository.execute("DELETE FROM PageComment t WHERE t.pageId=?0", pageId);
    }

    private CommentResponse response(PageComment comment) {
        CommentResponse response = new CommentResponse();
        response.id = comment.id;
        response.pageId = comment.pageId;
        response.userId = comment.userId;
        response.ip = comment.ip;
        response.status = comment.status;
        response.parentId = comment.parentId;
        response.firstParentId = comment.firstParentId;
        response.content = comment.content;
        response.totalReplies = comment.totalReplies;
        response.totalVoteUp = comment.totalVoteUp;
        response.totalVoteDown = comment.totalVoteDown;
        response.createdTime = comment.createdTime;
        response.createdBy = comment.createdBy;
        response.updatedTime = comment.updatedTime;
        response.updatedBy = comment.updatedBy;
        return response;
    }

}
