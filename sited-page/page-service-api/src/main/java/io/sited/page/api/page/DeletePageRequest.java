package io.sited.page.api.page;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DeletePageRequest {
    @XmlElementWrapper(name = "pages")
    @XmlElement(name = "page")
    public List<PageView> pages;

    @XmlElement(name = "requestBy")
    public String requestBy;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PageView {
        @XmlElement(name = "id")
        public String id;

        @XmlElement(name = "status")
        public PageStatus status;
    }
}
