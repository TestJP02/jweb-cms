package app.jweb.user.web;

import app.jweb.user.api.UserStatisticsWebService;
import app.jweb.user.api.user.UserChannelStatisticsView;
import app.jweb.user.domain.UserChannelStatistics;
import app.jweb.user.service.UserStatisticsService;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author miller
 */
public class UserStatisticsWebServiceImpl implements UserStatisticsWebService {
    @Inject
    UserStatisticsService userStatisticsService;

    @Override
    public List<UserChannelStatisticsView> channelStatistics() {
        return userStatisticsService.channelStatistics().stream().map(this::view)
            .collect(Collectors.toList());
    }

    private UserChannelStatisticsView view(UserChannelStatistics statistics) {
        UserChannelStatisticsView view = new UserChannelStatisticsView();
        view.channel = statistics.channel;
        view.total = statistics.total;
        return view;
    }
}
