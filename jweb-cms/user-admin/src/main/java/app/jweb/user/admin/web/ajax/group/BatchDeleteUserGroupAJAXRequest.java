package app.jweb.user.admin.web.ajax.group;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BatchDeleteUserGroupAJAXRequest {
    @NotNull
    @XmlElement(name = "ids")
    public List<String> ids;
}
