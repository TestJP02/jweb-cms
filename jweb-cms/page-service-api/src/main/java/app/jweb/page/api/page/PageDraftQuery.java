package app.jweb.page.api.page;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
public class PageDraftQuery {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "page")
    public Integer page = 1;
    @XmlElement(name = "limit")
    public Integer limit = Integer.MAX_VALUE;
    @XmlElement(name = "desc")
    public Boolean desc;
    @XmlElement(name = "sortingField")
    public String sortingField;
    @XmlElement(name = "status")
    public PageStatus status;
}
