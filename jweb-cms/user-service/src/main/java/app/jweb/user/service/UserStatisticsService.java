package app.jweb.user.service;

import app.jweb.database.Database;
import app.jweb.database.Query;
import app.jweb.user.api.user.UserStatus;
import app.jweb.user.domain.UserChannelStatistics;

import javax.inject.Inject;
import java.util.List;

/**
 * @author miller
 */
public class UserStatisticsService {
    @Inject
    Database database;

    public List<UserChannelStatistics> channelStatistics() {
        String sql = "SELECT t.channel AS t.channel, COUNT(*) AS t.total FROM User t WHERE t.status=?0 GROUP BY t.channel";
        Query<UserChannelStatistics> query = database.query(sql, UserChannelStatistics.class, UserStatus.ACTIVE);
        return query.find();
    }
}
