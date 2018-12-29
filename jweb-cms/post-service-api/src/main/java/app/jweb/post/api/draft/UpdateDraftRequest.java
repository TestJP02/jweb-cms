package app.jweb.post.api.draft;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;
import java.util.Map;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateDraftRequest {
    @XmlElement(name = "categoryId")
    @NotNull
    @Size(max = 36)
    public String categoryId;

    @XmlElement(name = "path")
    @NotNull
    @Size(max = 127)
    public String path;

    @XmlElement(name = "templatePath")
    public String templatePath;

    @XmlElement(name = "title")
    @NotNull
    @Size(max = 511)
    public String title;

    @XmlElement(name = "description")
    @Size(max = 511)
    public String description;

    @XmlElement(name = "content")
    public String content;

    @XmlElementWrapper(name = "tags")
    @XmlElement(name = "tag")
    public List<String> tags;

    @XmlElementWrapper(name = "keywords")
    @XmlElement(name = "keyword")
    public List<String> keywords;

    @XmlElement(name = "fields")
    public Map<String, String> fields;

    @XmlElement(name = "imageURL")
    @NotNull
    @Size(max = 255)
    public String imageURL;

    @XmlElement(name = "topFixed")
    public Boolean topFixed;

    @XmlElement(name = "requestBy")
    @Size(max = 63)
    public String requestBy;
}
