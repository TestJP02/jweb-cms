package app.jweb.comment.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentWebOptions {
    @XmlElement(name = "visitorCommentEnabled")
    public Boolean visitorCommentEnabled = false;
}
