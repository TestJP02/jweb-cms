package io.sited.page.search.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SearchPageResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "categoryId")
    public String categoryId;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "subTitle")
    public String subTitle;
    @XmlElement(name = "description")
    public String description;
    @XmlElementWrapper(name = "tags")
    @XmlElement(name = "tag")
    public List<String> tags;
    @XmlElement(name = "imageURL")
    public String imageURL;
    @XmlElement(name = "userId")
    public String userId;
    @XmlElement(name = "language")
    public String language;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
}
