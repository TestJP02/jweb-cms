package app.jweb.file.admin.web.api.file;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FileUploadAJAXResponse {
    @XmlElement(name = "directoryId")
    public String directoryId;

    @XmlElement(name = "path")
    public String path;
}
