package io.sited.page.admin.web.api.comment;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateCommentAJAXRequest {
    @NotNull
    @XmlElement(name = "pageId")
    public String pageId;

    @Size(max = 36)
    @XmlElement(name = "parentId")
    public String parentId;

    @NotNull
    @Size(max = 65535)
    @XmlElement(name = "content")
    public String content;
}
