package app.jweb.file.web;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class FileWebOptions {
    @XmlElement(name = "imageScalar")
    public String imageScalar = "java-2d";
    @XmlElement(name = "uploadEnabled")
    public Boolean uploadEnabled = true;
    @XmlElement(name = "dir")
    public String dir = "file";
    @XmlElement(name = "directoryEnabled")
    public Boolean directoryEnabled = true;
}
