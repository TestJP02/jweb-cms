package app.jweb.post.admin.web.api.directory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PostDirectoryResponse {
    @XmlElement(name = "directoryId")
    public String directoryId;
}
