package io.sited.page.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PopularPageQuery {
    @XmlElement(name = "page")
    public Integer page = 1;


    @XmlElement(name = "limit")
    public Integer limit = 10;

    @XmlElement(name = "type")
    public RankType type = RankType.DAILY;
}
