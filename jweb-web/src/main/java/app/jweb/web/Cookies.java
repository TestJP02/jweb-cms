package app.jweb.web;

import javax.ws.rs.core.NewCookie;

/**
 * @author chi
 */
public interface Cookies {
    static NewCookie newCookie(String name, String value, int maxAge) {
        return new NewCookie(name, value, "/", null, null, maxAge, false);
    }

    static NewCookie newCookie(String name, String value) {
        return new NewCookie(name, value, "/", null, null, Integer.MAX_VALUE, false);
    }

    static NewCookie newSessionCookie(String name, String value) {
        return new NewCookie(name, value, "/", null, null, -1, false);
    }

    static NewCookie removeCookie(String name) {
        return new NewCookie(name, null, "/", null, null, 0, false);
    }
}
