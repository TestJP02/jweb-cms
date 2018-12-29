package app.jweb.file.api.file;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FileQuery {
    @XmlElement(name = "directoryIds")
    public List<String> directoryIds;
    @XmlElement(name = "keywords")
    public String keywords;
    @XmlElement(name = "status")
    public FileStatus status;
    @XmlElement(name = "startUpdatedTime")
    public OffsetDateTime startUpdatedTime;
    @XmlElement(name = "endUpdatedTime")
    public OffsetDateTime endUpdatedTime;
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
