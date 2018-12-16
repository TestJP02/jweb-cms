package app.jweb.post.api.post;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PopularPostResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "fields")
    public Map<String, String> fields;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "totalLiked")
    public Integer totalLiked;
    @XmlElement(name = "totalDisliked")
    public Integer totalDisliked;
    @XmlElement(name = "totalCommented")
    public Integer totalCommented;
}
