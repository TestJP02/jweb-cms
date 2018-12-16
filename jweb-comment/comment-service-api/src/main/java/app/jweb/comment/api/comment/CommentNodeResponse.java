package app.jweb.comment.api.comment;

import app.jweb.util.collection.QueryResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentNodeResponse {
    @XmlElement(name = "comment")
    public CommentResponse comment;

    @XmlElement(name = "children")
    public QueryResponse<CommentNodeResponse> children;
}
