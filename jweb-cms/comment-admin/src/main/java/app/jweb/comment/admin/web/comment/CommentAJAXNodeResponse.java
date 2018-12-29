package app.jweb.comment.admin.web.comment;


import app.jweb.comment.api.comment.CommentStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentAJAXNodeResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "pageId")
    public String pageId;
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "ip")
    public String ip;
    @XmlElement(name = "status")
    public CommentStatus status;
    @XmlElement(name = "parentId")
    public String parentId;
    @XmlElement(name = "firstParentId")
    public String firstParentId;
    @XmlElement(name = "content")
    public String content;
    @XmlElement(name = "totalVoteUp")
    public Integer totalVoteUp;
    @XmlElement(name = "totalVoteDown")
    public Integer totalVoteDown;
    @XmlElement(name = "totalReplies")
    public Integer totalReplies;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
    @XmlElement(name = "children")
    public List<CommentAJAXNodeResponse> children;
}
