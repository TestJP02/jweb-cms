package app.jweb.post.api.vote;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class VotePostResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "postId")
    public String postId;
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "ip")
    public String ip;
    @XmlElement(name = "count")
    public Integer count;
    @XmlElement(name = "type")
    public VoteType type;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
}
