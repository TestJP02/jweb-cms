package io.sited.page.service.task;

import io.sited.page.service.PageStatisticsService;

import javax.inject.Inject;

/**
 * @author chi
 */
public class ResetMonthlyVisitedTask implements Runnable {
    @Inject
    PageStatisticsService pageStatisticsService;

    @Override
    public void run() {
        pageStatisticsService.resetMonthlyVisited();
    }
}
