package app.jweb.page.rating.api.rating;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RatingResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "pageId")
    public String pageId;
    @XmlElement(name = "averageScore")
    public Double averageScore;
    @XmlElement(name = "totalScored")
    public Integer totalScored;
    @XmlElement(name = "totalScored1")
    public Integer totalScored1;
    @XmlElement(name = "totalScored2")
    public Integer totalScored2;
    @XmlElement(name = "totalScored3")
    public Integer totalScored3;
    @XmlElement(name = "totalScored4")
    public Integer totalScored4;
    @XmlElement(name = "totalScored5")
    public Integer totalScored5;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createBy")
    public String createdBy;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
}
