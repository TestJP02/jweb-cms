package app.jweb.page.rating.web.web.rating;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RatingAJAXResponse {
    @XmlElement(name = "averageScore")
    public Double averageScore;
    @XmlElement(name = "totalScored")
    public Integer totalScored;
}
