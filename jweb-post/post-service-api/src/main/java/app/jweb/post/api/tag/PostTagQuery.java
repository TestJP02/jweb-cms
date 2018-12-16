package app.jweb.post.api.tag;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PostTagQuery {
    @XmlElement(name = "parentId")
    public String parentId;
    @XmlElement(name = "displayName")
    public String displayName;
    @XmlElement(name = "level")
    public Integer level;
    @XmlElement(name = "type")
    public String type;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "status")
    public PostTagStatus status;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
    @XmlElement(name = "sortingField")
    public String sortingField;
    @XmlElement(name = "desc")
    public Boolean desc;
}
