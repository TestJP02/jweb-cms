package app.jweb.user.api.user;


import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserQuery {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "status")
    public UserStatus status;
    @XmlElement(name = "userGroupId")
    public String userGroupId;
    @NotNull
    @XmlElement(name = "page")
    public Integer page;
    @NotNull
    @XmlElement(name = "limit")
    public Integer limit;
    @XmlElement(name = "sortingField")
    public String sortingField;
    @XmlElement(name = "desc")
    public Boolean desc;
}
