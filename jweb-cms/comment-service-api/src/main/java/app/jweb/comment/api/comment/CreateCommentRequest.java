package app.jweb.comment.api.comment;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CreateCommentRequest {
    @NotNull
    @XmlElement(name = "pageId")
    public String pageId;

    @Size(max = 36)
    @XmlElement(name = "userId")
    public String userId;

    @NotNull
    @Size(max = 255)
    @XmlElement(name = "ip")
    public String ip;

    @Size(max = 36)
    @XmlElement(name = "parentId")
    public String parentId;

    @Size(max = 65535)
    @XmlElement(name = "content")
    public String content;

    @NotNull
    @Size(max = 63)
    @XmlElement(name = "requestBy")
    public String requestBy;
}
