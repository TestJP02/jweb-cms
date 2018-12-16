package app.jweb.page.rating.api.rating;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RatingTrackingResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "pageId")
    public String pageId;
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "ip")
    public String ip;
    @XmlElement(name = "score")
    public Integer score;
    @XmlElement(name = "content")
    public String content;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createBy")
    public String createdBy;
}
