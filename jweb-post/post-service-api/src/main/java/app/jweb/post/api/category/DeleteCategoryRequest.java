package app.jweb.post.api.category;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteCategoryRequest {
    @XmlElement(name = "id")
    public List<String> ids;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
