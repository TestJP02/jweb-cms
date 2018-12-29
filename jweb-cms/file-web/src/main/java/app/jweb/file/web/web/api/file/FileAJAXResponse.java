package app.jweb.file.web.web.api.file;

import app.jweb.file.api.file.FileStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FileAJAXResponse {
    @XmlElement(name = "id")
    public String id;
    @XmlElement(name = "directoryId")
    public String directoryId;
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "fileName")
    public String fileName;
    @XmlElement(name = "length")
    public Long length;
    @XmlElement(name = "title")
    public String title;
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "tags")
    public List<String> tags;
    @XmlElement(name = "extension")
    public String extension;
    @XmlElement(name = "status")
    public FileStatus status;
    @XmlElement(name = "createdTime")
    public OffsetDateTime createdTime;
    @XmlElement(name = "createdBy")
    public String createdBy;
    @XmlElement(name = "updatedTime")
    public OffsetDateTime updatedTime;
    @XmlElement(name = "updatedBy")
    public String updatedBy;
}
