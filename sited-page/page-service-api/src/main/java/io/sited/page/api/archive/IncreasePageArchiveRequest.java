package io.sited.page.api.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class IncreasePageArchiveRequest {
    @XmlElement(name = "requestBy")
    public String requestBy;
}
