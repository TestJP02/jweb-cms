package app.jweb.post.admin.web.api.crawler;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CrawlRequest {
    @XmlElement(name = "url")
    public String url;
    @XmlElement(name = "cookies")
    public String cookies;
    @XmlElement(name = "categoryId")
    public String categoryId;
}
