package app.jweb.post.admin.web.api.post;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PostTagSuggestAJAXResponse {
    @XmlElement(name = "tags")
    public List<String> tags;
}
