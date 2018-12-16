package app.jweb.user.admin.web.ajax.user;

import app.jweb.user.api.user.UserStatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserFindAJAXRequest {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "status")
    public UserStatus status;
    @XmlElement(name = "userGroupId")
    public String userGroupId;
    @XmlElement(name = "page")
    public Integer page;
    @XmlElement(name = "limit")
    public Integer limit;
    @XmlElement(name = "sortingField")
    public String sortingField;
    @XmlElement(name = "desc")
    public Boolean desc;
}
