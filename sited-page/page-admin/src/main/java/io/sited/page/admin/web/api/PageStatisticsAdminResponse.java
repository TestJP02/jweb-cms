package io.sited.page.admin.web.api;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageStatisticsAdminResponse {
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "path")
    public String path;
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
}
