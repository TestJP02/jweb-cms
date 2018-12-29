package app.jweb.post.api.vote;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class VotePostRequest {
    @XmlElement(name = "postId")
    public String postId;
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "ip")
    public String ip;
    @XmlElement(name = "type")
    public VoteType type;
    @XmlElement(name = "count")
    public Integer count;
    @XmlElement(name = "requestBy")
    public String requestBy;
}
