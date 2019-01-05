package app.jweb.page.api.statistics;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageStatisticsQuery {
    @XmlElement(name = "limit")
    public Integer limit;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "sortingField")
    public String sortingField = "totalDailyVisited";
    @XmlElement(name = "desc")
    public Boolean desc = true;
}
