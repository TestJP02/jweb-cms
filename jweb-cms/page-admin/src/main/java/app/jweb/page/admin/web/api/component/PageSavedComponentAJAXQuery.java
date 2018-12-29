package app.jweb.page.admin.web.api.component;

import app.jweb.page.api.component.SavedComponentStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageSavedComponentAJAXQuery {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "status")
    public SavedComponentStatus status;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
}
