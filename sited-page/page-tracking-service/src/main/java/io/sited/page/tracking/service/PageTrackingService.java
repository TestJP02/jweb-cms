package io.sited.page.tracking.service;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.sited.database.Database;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.page.api.page.PageVisitedMessage;
import io.sited.page.tracking.api.tracking.PageStatisticsQuery;
import io.sited.page.tracking.api.tracking.PageStatisticsResponse;
import io.sited.page.tracking.api.tracking.PageTrackingQuery;
import io.sited.page.tracking.api.tracking.PageTrackingResponse;
import io.sited.page.tracking.api.tracking.PageTrackingType;
import io.sited.page.tracking.domain.PageDailyTracking;
import io.sited.page.tracking.domain.PageDailyTrackingView;
import io.sited.page.tracking.domain.PageMonthlyTracking;
import io.sited.page.tracking.domain.PageMonthlyTrackingView;
import io.sited.page.tracking.domain.PageTracking;
import io.sited.page.tracking.domain.PageTrackingView;
import io.sited.page.tracking.domain.PageWeeklyTracking;
import io.sited.page.tracking.domain.PageWeeklyTrackingView;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author chi
 */
public class PageTrackingService {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    @Inject
    Repository<PageTracking> repository;
    @Inject
    Repository<PageDailyTracking> dailyRepository;
    @Inject
    Repository<PageWeeklyTracking> weeklyRepository;
    @Inject
    Repository<PageMonthlyTracking> monthlyRepository;
    @Inject
    Database database;

    public List<PageStatisticsResponse> find(PageStatisticsQuery request) {
        PageTrackingType type = request.type == null ? PageTrackingType.DAILY : request.type;
        switch (type) {
            case DAILY:
                Query<PageDailyTrackingView> query = database.query("SELECT NEW io.sited.page.tracking.domain.PageDailyTrackingView(SUM(totalVisited) AS total,date AS timestamp) FROM PageDailyTracking WHERE 1=1 ", PageDailyTrackingView.class);
                int dailyIndex = 0;
                if (!Strings.isNullOrEmpty(request.categoryId)) {
                    query.append(String.format(" AND categoryId=?%d ", dailyIndex), request.categoryId);
                    dailyIndex++;
                }
                if (!Strings.isNullOrEmpty(request.pageId)) {
                    query.append(String.format(" AND pageId=?%d ", dailyIndex), request.pageId);
                    dailyIndex++;
                }
                if (request.startTime != null) {
                    String startDate = DATE_FORMATTER.format(request.startTime);
                    query.append(String.format(" AND date>=?%d ", dailyIndex), startDate);
                    dailyIndex++;
                }
                if (request.endTime != null) {
                    String endDate = DATE_FORMATTER.format(request.endTime);
                    query.append(String.format(" AND date<=?%d ", dailyIndex), endDate);
                    dailyIndex++;
                }
                query.append(" GROUP BY date ORDER BY date");
                query.limit(request.page, request.limit);
                return query.find().stream().map(this::response).collect(Collectors.toList());
            case WEEKLY:
                Query<PageWeeklyTrackingView> weeklyViewQuery = database.query("SELECT NEW io.sited.page.tracking.domain.PageWeeklyTrackingView(SUM(totalVisited) AS total,week AS timestamp) FROM PageWeeklyTracking WHERE 1=1 ", PageWeeklyTrackingView.class);
                int weeklyIndex = 0;
                if (!Strings.isNullOrEmpty(request.categoryId)) {
                    weeklyViewQuery.append(String.format(" AND categoryId=?%d ", weeklyIndex), request.categoryId);
                    weeklyIndex++;
                }
                if (!Strings.isNullOrEmpty(request.pageId)) {
                    weeklyViewQuery.append(String.format(" AND pageId=?%d ", weeklyIndex), request.pageId);
                    weeklyIndex++;
                }
                if (request.startTime != null) {
                    TemporalField weekField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                    int week = request.startTime.get(weekField);
                    String startWeek = request.startTime.getYear() + " w" + week;
                    weeklyViewQuery.append(String.format(" AND week>=?%d ", weeklyIndex), startWeek);
                    weeklyIndex++;
                }
                if (request.endTime != null) {
                    TemporalField weekField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                    int week = request.startTime.get(weekField);
                    String endWeek = request.startTime.getYear() + " w" + week;
                    weeklyViewQuery.append(String.format(" AND week<=?%d ", weeklyIndex), endWeek);
                    weeklyIndex++;
                }
                weeklyViewQuery.append(" GROUP BY week ORDER BY week");
                weeklyViewQuery.limit(request.page, request.limit);
                return weeklyViewQuery.find().stream().map(this::response).collect(Collectors.toList());
            case MONTHLY:
                Query<PageMonthlyTrackingView> monthlyViewQuery = database.query("SELECT NEW io.sited.page.tracking.domain.PageMonthlyTrackingView(SUM(totalVisited) AS total,month AS timestamp) FROM PageMonthlyTracking WHERE 1=1 ", PageMonthlyTrackingView.class);
                int monthIndex = 0;
                if (!Strings.isNullOrEmpty(request.categoryId)) {
                    monthlyViewQuery.append(String.format(" AND categoryId=?%d ", monthIndex), request.categoryId);
                    monthIndex++;
                }
                if (!Strings.isNullOrEmpty(request.pageId)) {
                    monthlyViewQuery.append(String.format(" AND pageId=?%d ", monthIndex), request.pageId);
                    monthIndex++;
                }
                if (request.startTime != null) {
                    String startMonth = MONTH_FORMATTER.format(request.startTime);
                    monthlyViewQuery.append(String.format(" AND month>=?%d ", monthIndex), startMonth);
                    monthIndex++;
                }
                if (request.endTime != null) {
                    String endMonth = MONTH_FORMATTER.format(request.endTime);
                    monthlyViewQuery.append(String.format(" AND month<=?%d ", monthIndex), endMonth);
                    monthIndex++;
                }
                monthlyViewQuery.append(" GROUP BY month ORDER BY month");
                monthlyViewQuery.limit(request.page, request.limit);
                return monthlyViewQuery.find().stream().map(this::response).collect(Collectors.toList());
            case OVERALL:
            default:
                int index = 0;
                Query<PageTrackingView> overallViewQuery = database.query("SELECT NEW io.sited.page.tracking.domain.PageTrackingView(SUM(totalVisited) AS total) FROM PageTracking WHERE 1=1 ", PageTrackingView.class);
                if (!Strings.isNullOrEmpty(request.categoryId)) {
                    overallViewQuery.append(String.format(" AND categoryId=?%d ", index), request.categoryId);
                    index++;
                }
                if (!Strings.isNullOrEmpty(request.pageId)) {
                    overallViewQuery.append(String.format(" AND pageId=?%d ", index), request.pageId);
                    index++;
                }
                overallViewQuery.limit(1, 1);
                return overallViewQuery.find().stream().map(this::response).collect(Collectors.toList());
        }
    }

    public QueryResponse<PageTrackingResponse> find(PageTrackingQuery pageTrackingQuery) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = Lists.newArrayList();
        int index = 0;
        if (!Strings.isNullOrEmpty(pageTrackingQuery.categoryId)) {
            sql.append(String.format(" AND categoryId=?%d ", index));
            params.add(pageTrackingQuery.categoryId);
        }
        if (Boolean.TRUE.equals(pageTrackingQuery.pageOnly)) {
            sql.append(" AND pageId is not null");
        }
        switch (pageTrackingQuery.type) {
            case DAILY:
                sql.insert(0, String.format("SELECT t FROM %s t WHERE 1=1 ", "PageDailyTracking"));
                return dailyRepository.query(sql.toString(), params.toArray())
                    .sort("totalVisited", true)
                    .limit(1, pageTrackingQuery.limit)
                    .findAll().map(this::response);
            case WEEKLY:
                sql.insert(0, String.format("SELECT t FROM %s t WHERE 1=1 ", "PageWeeklyTracking"));
                return weeklyRepository.query(sql.toString(), params.toArray())
                    .sort("totalVisited", true)
                    .limit(1, pageTrackingQuery.limit)
                    .findAll().map(this::response);
            case MONTHLY:
                sql.insert(0, String.format("SELECT t FROM %s t WHERE 1=1 ", "PageMonthlyTracking"));
                return monthlyRepository.query(sql.toString(), params.toArray())
                    .sort("totalVisited", true)
                    .limit(1, pageTrackingQuery.limit)
                    .findAll().map(this::response);
            case OVERALL:
            default:
                sql.insert(0, String.format("SELECT t FROM %s t WHERE 1=1 ", "PageTracking"));
                return repository.query(sql.toString(), params.toArray())
                    .sort("totalVisited", true)
                    .limit(1, pageTrackingQuery.limit)
                    .findAll().map(this::response);
        }
    }

    @Transactional
    public void track(PageVisitedMessage pageVisitedMessage) {
        int overallEffectRows = repository.execute("UPDATE PageTracking SET totalVisited=totalVisited+1 WHERE pageId=?0", pageVisitedMessage.pageId);
        if (overallEffectRows == 0) {
            initPageTracking(pageVisitedMessage.pageId, pageVisitedMessage.categoryId);
        }
        OffsetDateTime now = pageVisitedMessage.timestamp;
        TemporalField weekField = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
        int week = now.get(weekField);

        String dateString = DATE_FORMATTER.format(now);
        String weekString = now.getYear() + " W" + week;
        String monthString = MONTH_FORMATTER.format(now);
        int dailyEffectRows = dailyRepository.execute("UPDATE PageDailyTracking SET totalVisited=totalVisited+1 WHERE pageId=?0 AND date=?1", pageVisitedMessage.pageId, dateString);
        if (dailyEffectRows == 0) {
            initPageDailyTracking(dateString, pageVisitedMessage.pageId, pageVisitedMessage.categoryId);
        }
        int monthlyEffectRows = monthlyRepository.execute("UPDATE PageMonthlyTracking SET totalVisited=totalVisited+1 WHERE pageId=?0 AND month=?1", pageVisitedMessage.pageId, monthString);
        if (monthlyEffectRows == 0) {
            initPageMonthlyTracking(monthString, pageVisitedMessage.pageId, pageVisitedMessage.categoryId);
        }
        int weeklyEffectRows = weeklyRepository.execute("UPDATE PageWeeklyTracking SET totalVisited=totalVisited+1 WHERE pageId=?0 AND week=?1", pageVisitedMessage.pageId, weekString);
        if (weeklyEffectRows == 0) {
            initPageWeeklyTracking(weekString, pageVisitedMessage.pageId, pageVisitedMessage.categoryId);
        }
    }

    private void initPageTracking(String pageId, String categoryId) {
        PageTracking pageTracking = new PageTracking();
        pageTracking.id = UUID.randomUUID().toString();
        pageTracking.pageId = pageId;
        pageTracking.categoryId = categoryId;
        pageTracking.totalVisited = 1;
        pageTracking.createdTime = OffsetDateTime.now();
        pageTracking.createdBy = "PageVisitedMessage";
        repository.insert(pageTracking);
    }

    private void initPageDailyTracking(String date, String pageId, String categoryId) {
        PageDailyTracking pageDailyTracking = new PageDailyTracking();
        pageDailyTracking.id = UUID.randomUUID().toString();
        pageDailyTracking.pageId = pageId;
        pageDailyTracking.categoryId = categoryId;
        pageDailyTracking.date = date;
        pageDailyTracking.totalVisited = 1;
        pageDailyTracking.createdTime = OffsetDateTime.now();
        pageDailyTracking.createdBy = "PageVisitedMessage";
        dailyRepository.insert(pageDailyTracking);
    }

    private void initPageMonthlyTracking(String month, String pageId, String categoryId) {
        PageMonthlyTracking pageMonthlyTracking = new PageMonthlyTracking();
        pageMonthlyTracking.id = UUID.randomUUID().toString();
        pageMonthlyTracking.pageId = pageId;
        pageMonthlyTracking.categoryId = categoryId;
        pageMonthlyTracking.month = month;
        pageMonthlyTracking.totalVisited = 1;
        pageMonthlyTracking.createdTime = OffsetDateTime.now();
        pageMonthlyTracking.createdBy = "PageVisitedMessage";
        monthlyRepository.insert(pageMonthlyTracking);
    }

    private void initPageWeeklyTracking(String week, String pageId, String categoryId) {
        PageWeeklyTracking pageWeeklyTracking = new PageWeeklyTracking();
        pageWeeklyTracking.id = UUID.randomUUID().toString();
        pageWeeklyTracking.pageId = pageId;
        pageWeeklyTracking.categoryId = categoryId;
        pageWeeklyTracking.week = week;
        pageWeeklyTracking.totalVisited = 1;
        pageWeeklyTracking.createdTime = OffsetDateTime.now();
        pageWeeklyTracking.createdBy = "PageVisitedMessage";
        weeklyRepository.insert(pageWeeklyTracking);
    }

    private PageTrackingResponse response(PageTracking pageTracking) {
        PageTrackingResponse response = new PageTrackingResponse();
        response.id = pageTracking.id;
        response.pageId = pageTracking.pageId;
        response.categoryId = pageTracking.categoryId;
        response.totalVisited = pageTracking.totalVisited;
        response.createdTime = pageTracking.createdTime;
        response.createdBy = pageTracking.createdBy;
        return response;
    }

    private PageTrackingResponse response(PageDailyTracking pageTracking) {
        PageTrackingResponse response = new PageTrackingResponse();
        response.id = pageTracking.id;
        response.pageId = pageTracking.pageId;
        response.categoryId = pageTracking.categoryId;
        response.totalVisited = pageTracking.totalVisited;
        response.createdTime = pageTracking.createdTime;
        response.createdBy = pageTracking.createdBy;
        response.timestamp = pageTracking.date;
        return response;
    }

    private PageTrackingResponse response(PageWeeklyTracking pageTracking) {
        PageTrackingResponse response = new PageTrackingResponse();
        response.id = pageTracking.id;
        response.pageId = pageTracking.pageId;
        response.categoryId = pageTracking.categoryId;
        response.totalVisited = pageTracking.totalVisited;
        response.createdTime = pageTracking.createdTime;
        response.createdBy = pageTracking.createdBy;
        response.timestamp = pageTracking.week;
        return response;
    }

    private PageTrackingResponse response(PageMonthlyTracking pageTracking) {
        PageTrackingResponse response = new PageTrackingResponse();
        response.id = pageTracking.id;
        response.pageId = pageTracking.pageId;
        response.categoryId = pageTracking.categoryId;
        response.totalVisited = pageTracking.totalVisited;
        response.createdTime = pageTracking.createdTime;
        response.createdBy = pageTracking.createdBy;
        response.timestamp = pageTracking.month;
        return response;
    }

    private PageStatisticsResponse response(PageDailyTrackingView view) {
        PageStatisticsResponse response = new PageStatisticsResponse();
        response.total = view.total;
        response.timestamp = view.timestamp;
        return response;
    }

    private PageStatisticsResponse response(PageWeeklyTrackingView view) {
        PageStatisticsResponse response = new PageStatisticsResponse();
        response.total = view.total;
        response.timestamp = view.timestamp;
        return response;
    }

    private PageStatisticsResponse response(PageMonthlyTrackingView view) {
        PageStatisticsResponse response = new PageStatisticsResponse();
        response.total = view.total;
        response.timestamp = view.timestamp;
        return response;
    }

    private PageStatisticsResponse response(PageTrackingView view) {
        PageStatisticsResponse response = new PageStatisticsResponse();
        response.total = view.total;
        return response;
    }
}
