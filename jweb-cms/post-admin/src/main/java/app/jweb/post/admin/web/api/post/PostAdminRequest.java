package app.jweb.post.admin.web.api.post;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PostAdminRequest {
    @XmlElement(name = "id")
    public String id;

    @XmlElement(name = "postId")
    public String postId;

    @NotNull
    @XmlElement(name = "categoryId")
    public String categoryId;

    @NotNull
    @XmlElement(name = "path")
    public String path;

    @NotNull
    @XmlElement(name = "templatePath")
    public String templatePath;

    @XmlElement(name = "version")
    public Integer version;
    @NotNull
    @XmlElement(name = "title")
    public String title;

    @XmlElement(name = "imageURL")
    public String imageURL;

    @XmlElement(name = "keywords")
    public List<String> keywords;

    @XmlElement(name = "tags")
    public List<String> tags;

    @XmlElement(name = "fields")
    public Map<String, String> fields;

    @XmlElement(name = "description")
    public String description;

    @XmlElement(name = "content")
    public String content;

    @XmlElement(name = "topFixed")
    public Boolean topFixed;
}
