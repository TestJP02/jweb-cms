package io.sited.file.admin.web.ajax.file;

import io.sited.file.api.file.FileStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FileFindAJAXRequest {
    @XmlElement(name = "keywords")
    public String keywords;
    @XmlElement(name = "directoryId")
    public String directoryId;
    @XmlElement(name = "status")
    public FileStatus status;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
    @XmlElement(name = "sortingField")
    public String sortingField;
    @XmlElement(name = "desc")
    public Boolean desc;
}
