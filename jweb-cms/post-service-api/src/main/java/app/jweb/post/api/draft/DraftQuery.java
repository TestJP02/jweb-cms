package app.jweb.post.api.draft;

import app.jweb.post.api.post.PostStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DraftQuery {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "userId")
    public String userId;
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
    public PostStatus status;
}
