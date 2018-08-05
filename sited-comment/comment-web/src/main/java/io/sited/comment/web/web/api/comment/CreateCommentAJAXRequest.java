package io.sited.comment.web.web.api.comment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateCommentAJAXRequest {
    @XmlElement(name = "pageId")
    public String pageId;

    @XmlElement(name = "parentId")
    public String parentId;

    @XmlElement(name = "content")
    public String content;
}
