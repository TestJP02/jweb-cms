package app.jweb.file.api.file;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CreateFileRequest {
    @NotNull
    @Size(max = 63)
    @XmlElement(name = "directoryId")
    public String directoryId;
    @NotNull
    @Size(max = 255)
    @XmlElement(name = "path")
    public String path;
    @XmlElement(name = "length")
    public Long length;
    @XmlElement(name = "fileName")
    public String fileName;
    @Size(max = 255)
    @XmlElement(name = "title")
    public String title;
    @Size(max = 255)
    @XmlElement(name = "description")
    public String description;
    @XmlElement(name = "tags")
    public List<String> tags;
    @NotNull
    @Size(max = 63)
    @XmlElement(name = "requestBy")
    public String requestBy;
}

