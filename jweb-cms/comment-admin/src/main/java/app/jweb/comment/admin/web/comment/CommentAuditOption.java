package app.jweb.comment.admin.web.comment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentAuditOption {
    @XmlElement(name = "enabled")
    public Boolean enabled;
}
