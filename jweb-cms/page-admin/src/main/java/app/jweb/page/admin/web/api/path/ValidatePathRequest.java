package app.jweb.page.admin.web.api.path;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ValidatePathRequest {
    @XmlElement(name = "draftId")
    public String draftId;

    @XmlElement(name = "path")
    public String path;
}
