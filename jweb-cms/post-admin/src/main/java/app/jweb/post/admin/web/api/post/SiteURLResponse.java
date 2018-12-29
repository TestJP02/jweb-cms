package app.jweb.post.admin.web.api.post;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SiteURLResponse {
    @XmlElement(name = "siteURL")
    public String siteURL;
}
