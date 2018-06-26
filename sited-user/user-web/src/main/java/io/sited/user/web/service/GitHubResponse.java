package io.sited.user.web.service;

import io.sited.user.api.oauth.Provider;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GitHubResponse implements OauthResponse {
    @XmlElement(name = "login")
    public String login;
    @XmlElement(name = "email")
    public String email;
    @XmlElement(name = "createdAt")
    public OffsetDateTime createdTime;
    @XmlElement(name = "updated_at")
    public OffsetDateTime updatedTime;

    @Override
    public String username() {
        return login;
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
        return Provider.GITHUB;
    }

    @Override
    public OffsetDateTime createdTime() {
        return createdTime;
    }

    @Override
    public OffsetDateTime updatedTime() {
        return updatedTime;
    }
}
