package app.jweb.file.api.file;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FileListQuery {
    @XmlElement(name = "directoryId")
    public String directoryId;
    @XmlElement(name = "query")
    public String query;
    @NotNull
    @Size(min = 1)
    @XmlElement(name = "page")
    public Integer page;
    @NotNull
    @Size(min = 1)
    @XmlElement(name = "limit")
    public Integer limit;
    @XmlElement(name = "sortingField")
    public String sortingField;
    @XmlElement(name = "desc")
    public Boolean desc;
}
