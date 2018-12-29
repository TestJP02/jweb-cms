package app.jweb.page.admin.web.api.component;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchDeletePageSavedComponentRequest {
    @XmlElement(name = "ids")
    public List<String> ids;
}
