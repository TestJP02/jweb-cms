package io.sited.page.web.web.api.comment;

import io.sited.util.collection.QueryResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentNodesContent {
    @XmlElement(name = "topComments")
    public QueryResponse<ParentCommentAJAXResponse> topComments;
    @XmlElement(name = "remainNum")
    public Integer remainNum;
}
