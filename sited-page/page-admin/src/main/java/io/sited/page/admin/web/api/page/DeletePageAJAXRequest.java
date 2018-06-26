package io.sited.page.admin.web.api.page;

import io.sited.page.api.page.DeletePageRequest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DeletePageAJAXRequest {
    @XmlElement(name = "pages")
    public List<DeletePageRequest.PageView> pages;
}
