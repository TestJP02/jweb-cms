package app.jweb.user.admin.web.ajax;

import app.jweb.user.admin.web.ajax.user.UserStatisticsAJAXResponse;
import app.jweb.user.api.UserStatisticsWebService;
import app.jweb.user.api.UserWebService;
import app.jweb.user.api.user.UserChannelStatisticsView;
import app.jweb.user.api.user.UserQuery;
import app.jweb.user.api.user.UserStatus;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

/**
 * @author miller
 */
@Path("/admin/api/user/statistics")
public class UserStatisticsAdminAJAXController {
    @Inject
    UserStatisticsWebService userStatisticsWebService;
    @Inject
    UserWebService userWebService;

    @Path("")
    @GET
    public Response statistics() {
        UserStatisticsAJAXResponse response = new UserStatisticsAJAXResponse();
        UserQuery userQuery = new UserQuery();
        userQuery.status = UserStatus.ACTIVE;
        userQuery.page = 1;
        userQuery.limit = Integer.MAX_VALUE;
        response.total = userWebService.find(userQuery).total;
        response.channels = userStatisticsWebService.channelStatistics()
            .stream().map(this::channel).collect(Collectors.toList());
        return Response.ok(response).build();
    }

    private UserStatisticsAJAXResponse.Channel channel(UserChannelStatisticsView view) {
        UserStatisticsAJAXResponse.Channel channel = new UserStatisticsAJAXResponse.Channel();
        channel.channel = view.channel;
        channel.total = view.total;
        return channel;
    }
}
