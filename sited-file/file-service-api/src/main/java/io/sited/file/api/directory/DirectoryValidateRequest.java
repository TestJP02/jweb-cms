package io.sited.file.api.directory;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType
public class DirectoryValidateRequest {
    @NotNull
    @XmlElement(name = "path")
    public String path;
    @NotNull
    @XmlElement(name = "name")
    public String name;
    @NotNull
    @XmlElement(name = "parameterTypes")
    public Class[] parameterTypes;
}
