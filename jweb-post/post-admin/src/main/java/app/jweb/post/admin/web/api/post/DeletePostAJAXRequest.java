package app.jweb.post.admin.web.api.post;

import app.jweb.post.api.post.DeletePostRequest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DeletePostAJAXRequest {
    @XmlElement(name = "posts")
    public List<DeletePostRequest.PostView> posts;
}
