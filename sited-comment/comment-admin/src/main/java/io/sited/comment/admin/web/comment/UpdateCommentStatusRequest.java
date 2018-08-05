package io.sited.comment.admin.web.comment;


import io.sited.comment.api.comment.CommentStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateCommentStatusRequest {
    @XmlElement(name = "status")
    public CommentStatus status;
}
