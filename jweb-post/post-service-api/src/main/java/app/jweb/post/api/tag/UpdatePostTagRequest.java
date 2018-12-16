package app.jweb.post.api.tag;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdatePostTagRequest {
    @XmlElement(name = "parentId")
    public String parentId;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "fields")
    public Map<String, String> fields;
    @XmlElement(name = "alias")
    public String alias;
    @XmlElement(name = "type")
    public String type;
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "displayName")
    public String displayName;
    @XmlElement(name = "displayOrder")
    public Integer displayOrder;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "totalTagged")
    public Integer totalTagged;
    @XmlElement(name = "status")
    public PostTagStatus status;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
