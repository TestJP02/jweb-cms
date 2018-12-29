package app.jweb.comment.api.comment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class VoteCommentRequest {
    @NotNull
    @Size(max = 36)
    @XmlElement(name = "userId")
    public String userId;

    @NotNull
    @Size(max = 36)
    @XmlElement(name = "ip")
    public String ip;

    @NotNull
    @Size(max = 63)
    @XmlElement(name = "requestBy")
    public String requestBy;
}
