package io.sited.page.admin.web.api.comment;


import io.sited.page.api.comment.CommentStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentFindAJAXRequest {
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "status")
    public CommentStatus status;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
}
