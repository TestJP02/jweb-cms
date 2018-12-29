package app.jweb.post.admin.web.api.category;

import app.jweb.post.api.category.CategoryStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryAJAXQuery {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
    @XmlElement(name = "status")
    public CategoryStatus status;
}
