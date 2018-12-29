package app.jweb.page.search.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchPageQuery {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "highlightEnabled")
    public Boolean highlightEnabled;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
    @XmlElement(name = "sortingField")
    public String sortingField;
    @XmlElement(name = "desc")
    public Boolean desc;
}
