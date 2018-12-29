package app.jweb.post.admin.web.api.post;

import app.jweb.post.api.post.PostStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PostAdminQuery {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
    @XmlElement(name = "desc")
    public Boolean desc = true;
    @XmlElement(name = "sortingField")
    public String sortingField = "updatedTime";
    @XmlElement(name = "status")
    public PostStatus status;
}
