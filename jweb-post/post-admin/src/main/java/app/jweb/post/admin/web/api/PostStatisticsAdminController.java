package app.jweb.post.admin.web.api;

import app.jweb.post.api.PostStatisticsWebService;
import app.jweb.post.api.PostWebService;
import app.jweb.post.api.post.BatchGetPostRequest;
import app.jweb.post.api.post.PostResponse;
import app.jweb.post.api.statistics.PostStatisticsQuery;
import app.jweb.post.api.statistics.PostStatisticsResponse;
import app.jweb.util.collection.QueryResponse;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chi
 */
@Path("/admin/api/post/statistics")
public class PostStatisticsAdminController {
    @Inject
    PostStatisticsWebService postStatisticsWebService;

    @Inject
    PostWebService postWebService;

    @RolesAllowed("LIST")
    @Path("/most-visited")
    @PUT
    public QueryResponse<PostStatisticsAdminResponse> tree(PostStatisticsQuery query) {
        QueryResponse<PostStatisticsResponse> statisticsResponses = postStatisticsWebService.find(query);
        List<String> postIds = statisticsResponses.items.stream().map(item -> item.id).collect(Collectors.toList());
        BatchGetPostRequest batchGetPostRequest = new BatchGetPostRequest();
        batchGetPostRequest.ids = postIds;
        Map<String, PostResponse> posts = postWebService.batchGet(batchGetPostRequest).stream().collect(Collectors.toMap(post -> post.id, post -> post));
        QueryResponse<PostStatisticsAdminResponse> adminResponses = new QueryResponse<>();
        adminResponses.total = statisticsResponses.total;
        adminResponses.limit = statisticsResponses.limit;
        adminResponses.page = statisticsResponses.page;
        adminResponses.items = statisticsResponses.items.stream().map(item -> {
            PostStatisticsAdminResponse response = new PostStatisticsAdminResponse();
            PostResponse post = posts.get(item.id);
            response.path = post.path;
            response.title = post.title;
            response.totalCommented = item.totalCommented;
            response.totalVisited = item.totalVisited;
            response.totalDailyVisited = item.totalDailyVisited;
            response.totalWeeklyVisited = item.totalWeeklyVisited;
            response.totalMonthlyVisited = item.totalMonthlyVisited;
            return response;
        }).collect(Collectors.toList());
        return adminResponses;
    }
}
