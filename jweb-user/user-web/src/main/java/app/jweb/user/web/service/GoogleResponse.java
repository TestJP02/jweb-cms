package app.jweb.user.web.service;

import app.jweb.user.api.oauth.Provider;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GoogleResponse implements OauthResponse {
    @XmlElement(name = "email")
    public String email;

    @XmlElement(name = "name")
    public String name;

    @Override
    public String nickname() {
        return name;
    }

    @Override
    public String username() {
        return email;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String phone() {
        return null;
    }

    @Override
    public Provider provider() {
        return Provider.GOOGLE;
    }

    @Override
    public OffsetDateTime createdTime() {
        return null;
    }

    @Override
    public OffsetDateTime updatedTime() {
        return null;
    }
}
