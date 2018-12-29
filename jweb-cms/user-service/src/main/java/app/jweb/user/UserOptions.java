package app.jweb.user;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class UserOptions {
    @XmlElement(name = "passwordChangedEmail")
    public EmailTemplateOptions passwordChangedEmail;

    @XmlElement(name = "defaultAdminUser")
    public DefaultAdminUser defaultAdminUser;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DefaultAdminUser {
        @XmlElement(name = "username")
        public String username = "admin";
        @XmlElement(name = "nickname")
        public String nickname = "Admin";
        @XmlElement(name = "description")
        public String description = "";
        @XmlElement(name = "email")
        public String email = "admin@admin.dev";
        @XmlElement(name = "phone")
        public String phone = "";
        @XmlElement(name = "password")
        public String password = "admin123!A";
    }
}
