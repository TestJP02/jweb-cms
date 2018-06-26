package io.sited.page.api.comment;

import io.sited.util.collection.QueryResponse;

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
