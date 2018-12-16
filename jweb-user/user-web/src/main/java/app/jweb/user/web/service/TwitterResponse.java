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
public class TwitterResponse implements OauthResponse {
    @XmlElement(name = "screen_name")
    public String screenName;
    @XmlElement(name = "name")
    public String name;
    @XmlElement(name = "email")
    public String email;
    @XmlElement(name = "created_at")
    public String createdAt;

    @Override
    public String nickname() {
        return name;
    }

    @Override
    public String username() {
        return screenName;
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
        return Provider.TWITTER;
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
