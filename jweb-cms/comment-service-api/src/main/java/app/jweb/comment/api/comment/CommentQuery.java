package app.jweb.comment.api.comment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentQuery {
    @NotNull
    @Size(max = 36)
    @XmlElement(name = "pageId")
    public String pageId;
    @XmlElement(name = "parentId")
    public String parentId;
    @XmlElement(name = "firstParentId")
    public String firstParentId;
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "status")
    public CommentStatus status;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
    @XmlElement(name = "sortingField")
    public String sortingField;
    @XmlElement(name = "desc")
    public Boolean desc;
}
