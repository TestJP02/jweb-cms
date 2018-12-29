package app.jweb.comment.web.web.api.comment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ParentCommentAJAXResponse {
    @XmlElement(name = "comment")
    public CommentAJAXResponse comment;
    @XmlElement(name = "children")
    public CommentPageAJAXResponse children;
    @XmlElement(name = "remainNum")
    public Integer remainNum;
}
