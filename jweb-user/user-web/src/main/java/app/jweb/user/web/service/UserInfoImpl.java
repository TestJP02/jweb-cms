package app.jweb.user.web.service;

import com.google.common.collect.ImmutableList;
import app.jweb.web.UserInfo;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.List;
import java.util.Set;

/**
 * @author chi
 */
public class UserInfoImpl implements UserInfo, SecurityContext {
    public String id;
    public String username;
    public String nickname;
    public String description;
    public String phone;
    public String email;
    public String imageURL;
    public Set<String> roles;
    public boolean authenticated;

    @Override
    public String id() {
        return id;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String nickname() {
        return nickname;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String phone() {
        return phone;
    }

    @Override
    public List<String> roles() {
        return ImmutableList.copyOf(roles);
    }

    @Override
    public String imageURL() {
        return imageURL;
    }

    @Override
    public boolean hasRole(String s) {
        return roles.contains(s);
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public Principal getUserPrincipal() {
        if (isAuthenticated()) {
            return () -> username;
        }
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        return roles.contains("*") || roles.contains(role);
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return "FORM_AUTH";
    }
}
