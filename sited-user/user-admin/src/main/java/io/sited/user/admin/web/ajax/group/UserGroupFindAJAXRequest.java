package io.sited.user.admin.web.ajax.group;

import io.sited.user.api.user.UserGroupStatus;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserGroupFindAJAXRequest {
    @XmlElement(name = "query")
    public String query;
    @XmlElement(name = "status")
    public UserGroupStatus status;
    @NotNull
    @XmlElement(name = "page")
    public Integer page;
    @NotNull
    @XmlElement(name = "limit")
    public Integer limit;
}
