package app.jweb.user.web.web.ajax.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UpdateUserAJAXRequest {
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "nickname")
    public String nickname;
    @XmlElement(name = "description")
    public String description;
}

