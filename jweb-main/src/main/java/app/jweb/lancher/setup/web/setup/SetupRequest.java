package app.jweb.lancher.setup.web.setup;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SetupRequest {
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "language")
    public String language;
    @XmlElement(name = "database")
    public DatabaseView database;
    @XmlElement(name = "user")
    public UserView user;

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class DatabaseView {
        @XmlElement(name = "vendor")
        public String vendor;
        @XmlElement(name = "host")
        public String host;
        @XmlElement(name = "port")
        public Integer port;
        @XmlElement(name = "username")
        public String username;
        @XmlElement(name = "password")
        public String password;
        @XmlElement(name = "database")
        public String database;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class UserView {
        @XmlElement(name = "username")
        public String username;
        @XmlElement(name = "password")
        public String password;
        @XmlElement(name = "email")
        public String email;
    }
}
