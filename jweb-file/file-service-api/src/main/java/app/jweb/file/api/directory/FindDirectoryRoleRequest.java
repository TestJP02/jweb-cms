package app.jweb.file.api.directory;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType
public class FindDirectoryRoleRequest {
    @NotNull
    @XmlElement(name = "path")
    public String path;
}
