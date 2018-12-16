package app.jweb.post.api.post;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DeletePostRequest {
    @XmlElementWrapper(name = "posts")
    @XmlElement(name = "post")
    public List<PostView> posts;

    @XmlElement(name = "requestBy")
    public String requestBy;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PostView {
        @XmlElement(name = "id")
        public String id;

        @XmlElement(name = "status")
        public PostStatus status;
    }
}
