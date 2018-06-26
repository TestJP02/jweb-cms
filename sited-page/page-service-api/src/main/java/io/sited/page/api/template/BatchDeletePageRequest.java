package io.sited.page.api.template;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchDeletePageRequest {
    @XmlElementWrapper(name = "ids")
    @XmlElement(name = "id")
    public List<String> ids;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
