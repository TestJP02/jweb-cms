package io.sited.file.api.file;

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
public class UpdateFileRequest {
    @XmlElement(name = "fileName")
    public String fileName;
    @Size(max = 255)
    @XmlElement(name = "length")
    public Long length;

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
