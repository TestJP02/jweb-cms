package io.sited.email.api.template;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EmailTemplateQuery {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "status")
    public EmailTemplateStatus status;
    @XmlElement(name = "sortingField")
    public String sortingField = "updatedTime";
    @XmlElement(name = "desc")
    public Boolean desc = true;
    @XmlElement(name = "page")
    public Integer page = 1;
    @XmlElement(name = "limit")
    public Integer limit = 10;
}
