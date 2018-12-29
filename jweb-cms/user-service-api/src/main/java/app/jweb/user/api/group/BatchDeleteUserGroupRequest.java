package app.jweb.user.api.group;


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
public class BatchDeleteUserGroupRequest {
    @NotNull
    @XmlElement(name = "ids")
    public List<String> ids;
    @NotNull
    @Size(max = 63)
    @XmlElement(name = "requestBy")
    public String requestBy;
}
