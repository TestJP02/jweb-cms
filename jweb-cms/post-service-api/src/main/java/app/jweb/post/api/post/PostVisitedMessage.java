package app.jweb.post.api.post;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PostVisitedMessage {
    @XmlElement(name = "postId")
    public String postId;

    @XmlElement(name = "categoryId")
    public String categoryId;

    @XmlElement(name = "userId")
    public String userId;

    @XmlElement(name = "clientId")
    public String clientId;

    @XmlElement(name = "timestamp")
    public OffsetDateTime timestamp;

    @XmlElement(name = "requestBy")
    public String requestBy;
}
