package app.jweb.post.web;

import app.jweb.post.api.PostStatisticsWebService;
import app.jweb.post.api.statistics.BatchGetPostStatisticsRequest;
import app.jweb.post.api.statistics.PostStatisticsQuery;
import app.jweb.post.api.statistics.PostStatisticsResponse;
import app.jweb.post.api.statistics.UpdatePostStatisticsRequest;
import app.jweb.post.domain.PostStatistics;
import app.jweb.post.service.PostStatisticsService;
import app.jweb.util.collection.QueryResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PostStatisticsWebServiceImpl implements PostStatisticsWebService {
    @Inject
    PostStatisticsService postStatisticsService;

    @Override
    public QueryResponse<PostStatisticsResponse> find(PostStatisticsQuery query) {
        return postStatisticsService.find(query).map(this::response);
    }

    @Override
    public List<PostStatisticsResponse> batchGet(BatchGetPostStatisticsRequest request) {
        List<PostStatistics> list = postStatisticsService.batchGet(request);
        return list.stream().map(this::response).collect(Collectors.toList());
    }

    @Override
    public Optional<PostStatisticsResponse> findById(String postId) {
        return postStatisticsService.findById(postId).map(this::response);
    }

    @Override
    public PostStatisticsResponse update(String postId, UpdatePostStatisticsRequest request) {
        PostStatistics postStatistics = postStatisticsService.update(postId, request);
        return response(postStatistics);
    }

    private PostStatisticsResponse response(PostStatistics postStatistics) {
        PostStatisticsResponse response = new PostStatisticsResponse();
        response.id = postStatistics.id;
        response.categoryId = postStatistics.categoryId;
        response.totalVisited = postStatistics.totalVisited;
        response.totalDailyVisited = postStatistics.totalDailyVisited;
        response.totalWeeklyVisited = postStatistics.totalWeeklyVisited;
        response.totalMonthlyVisited = postStatistics.totalMonthlyVisited;
        response.totalLiked = postStatistics.totalLiked;
        response.totalDailyLiked = postStatistics.totalDailyLiked;
        response.totalWeeklyLiked = postStatistics.totalWeeklyLiked;
        response.totalMonthlyLiked = postStatistics.totalMonthlyLiked;
        response.totalCommented = postStatistics.totalCommented;
        response.totalDisliked = postStatistics.totalDisliked;
        response.createdTime = postStatistics.createdTime;
        response.createdBy = postStatistics.createdBy;
        response.updatedTime = postStatistics.updatedTime;
        response.updatedBy = postStatistics.updatedBy;
        return response;
    }
}
