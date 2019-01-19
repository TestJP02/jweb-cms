package app.jweb.user.api;

import app.jweb.user.api.user.UserChannelStatisticsView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @author miller
 */
@Path("/api/user/statistics")
public interface UserStatisticsWebService {
    @Path("/channel")
    @GET
    List<UserChannelStatisticsView> channelStatistics();
}
