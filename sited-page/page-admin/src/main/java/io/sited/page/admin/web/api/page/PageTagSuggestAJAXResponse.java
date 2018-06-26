package io.sited.page.admin.web.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PageTagSuggestAJAXResponse {
    @XmlElement(name = "tags")
    public List<String> tags;
}
