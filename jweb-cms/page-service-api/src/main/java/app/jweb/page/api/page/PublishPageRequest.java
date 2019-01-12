package app.jweb.page.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PublishPageRequest {
    @XmlElement(name = "draftId")
    public String draftId;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
