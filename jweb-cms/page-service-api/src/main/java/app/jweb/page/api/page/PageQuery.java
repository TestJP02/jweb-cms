package app.jweb.page.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageQuery {
    @XmlElement(name = "templatePath")
    public String templatePath;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "page")
    public Integer page = 1;
    @XmlElement(name = "limit")
    public Integer limit = Integer.MAX_VALUE;
    @XmlElement(name = "desc")
    public Boolean desc;
    @XmlElement(name = "sortingField")
    public String sortingField;
    @XmlElement(name = "status")
    public PageStatus status;
}
