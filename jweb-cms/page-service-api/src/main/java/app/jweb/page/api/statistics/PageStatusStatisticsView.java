package app.jweb.page.api.statistics;

import app.jweb.page.api.page.PageStatus;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author miller
 */
public class PageStatusStatisticsView {
    @XmlElement(name = "status")
    public PageStatus status;
    @XmlElement(name = "total")
    public Long total;
}
