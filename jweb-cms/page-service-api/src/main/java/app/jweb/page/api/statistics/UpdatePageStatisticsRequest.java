package app.jweb.page.api.statistics;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
public class UpdatePageStatisticsRequest {
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "totalVisited")
    public Integer totalVisited;
    @XmlElement(name = "totalDailyVisited")
    public Integer totalDailyVisited;
    @XmlElement(name = "totalWeeklyVisited")
    public Integer totalWeeklyVisited;
    @XmlElement(name = "totalMonthlyVisited")
    public Integer totalMonthlyVisited;
    @XmlElement(name = "totalCommented")
    public Integer totalCommented;
    @XmlElement(name = "totalLiked")
    public Integer totalLiked;
    @XmlElement(name = "totalDailyLiked")
    public Integer totalDailyLiked;
    @XmlElement(name = "totalWeeklyLiked")
    public Integer totalWeeklyLiked;
    @XmlElement(name = "totalMonthlyLiked")
    public Integer totalMonthlyLiked;
    @XmlElement(name = "totalDisliked")
    public Integer totalDisliked;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
