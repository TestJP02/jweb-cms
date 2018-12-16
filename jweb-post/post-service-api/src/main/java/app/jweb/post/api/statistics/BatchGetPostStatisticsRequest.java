package app.jweb.post.api.statistics;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchGetPostStatisticsRequest {
    @XmlElement(name = "ids")
    public List<String> ids;
}
