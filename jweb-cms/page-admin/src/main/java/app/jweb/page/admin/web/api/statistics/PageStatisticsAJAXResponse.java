package app.jweb.page.admin.web.api.statistics;

import app.jweb.page.api.page.PageStatus;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author miller
 */
public class PageStatisticsAJAXResponse {
    @XmlElement(name = "total")
    public Long total;
    @XmlElement(name = "pages")
    public List<PageStatusStatistics> pages;

    public static class PageStatusStatistics {
        @XmlElement(name = "status")
        public PageStatus status;
        @XmlElement(name = "total")
        public Long total;
    }
}
