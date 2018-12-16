package app.jweb.user.web.util;

import java.util.regex.Pattern;

/**
 * @author chi
 */
public interface Email {
    Pattern EMAIL = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");

    static boolean isEmail(String email) {
        return EMAIL.matcher(email).matches();
    }
}
