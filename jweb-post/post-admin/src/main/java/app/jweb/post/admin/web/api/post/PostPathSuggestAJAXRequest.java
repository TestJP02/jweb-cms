package app.jweb.post.admin.web.api.post;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PostPathSuggestAJAXRequest {
    @NotNull
    @XmlElement(name = "language")
    public String language;
    @NotNull
    @XmlElement(name = "title")
    public String title;
}
