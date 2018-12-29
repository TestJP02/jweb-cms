package app.jweb.page.search.api.history;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchHistoryQuery {
    @XmlElement(name = "keywords")
    public String keywords;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
}
