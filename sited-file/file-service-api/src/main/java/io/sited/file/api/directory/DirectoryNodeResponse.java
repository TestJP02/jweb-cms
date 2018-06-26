package io.sited.file.api.directory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DirectoryNodeResponse {
    @XmlElement(name = "directory")
    public DirectoryResponse directory;
    @XmlElement(name = "children")
    public List<DirectoryNodeResponse> children;
}
