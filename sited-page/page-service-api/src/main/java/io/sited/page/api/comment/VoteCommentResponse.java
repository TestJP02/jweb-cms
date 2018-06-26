package io.sited.page.api.comment;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class VoteCommentResponse {
    @XmlElement(name = "totalVotes")
    public Integer totalVotes;
}
