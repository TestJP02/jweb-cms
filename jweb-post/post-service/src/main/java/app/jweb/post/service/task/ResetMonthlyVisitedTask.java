package app.jweb.post.service.task;

import app.jweb.post.service.PostStatisticsService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class ResetMonthlyVisitedTask implements Runnable {
    @Inject
    PostStatisticsService postStatisticsService;

    @Override
    public void run() {
        postStatisticsService.resetMonthlyVisited();
    }
}
