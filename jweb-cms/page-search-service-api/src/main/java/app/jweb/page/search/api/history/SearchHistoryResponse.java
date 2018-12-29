package app.jweb.page.search.api.history;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchHistoryResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "keywords")
    public String keywords;
    @XmlElement(name = "ip")
    public String ip;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
}
