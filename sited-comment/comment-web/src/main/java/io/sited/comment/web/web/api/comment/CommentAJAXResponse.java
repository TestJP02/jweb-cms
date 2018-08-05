package io.sited.comment.web.web.api.comment;


import io.sited.comment.api.comment.CommentStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentAJAXResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "topicId")
    public String topicId;
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "status")
    public CommentStatus status;
    @XmlElement(name = "parentId")
    public String parentId;
    @XmlElement(name = "firstParentId")
    public String firstParentId;
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "content")
    public String content;
    @XmlElement(name = "totalVotes")
    public Integer totalVotes;
    @XmlElement(name = "totalReplies")
    public Integer totalReplies;
    @XmlElement(name = "imageURLs")
    public List<String> imageURLs;
    @XmlElement(name = "attachmentURLs")
    public List<String> attachmentURLs;
    @XmlElement(name = "linkURLs")
    public List<String> linkURLs;
    @XmlElement(name = "createdDate")
    public OffsetDateTime createdDate;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
}
