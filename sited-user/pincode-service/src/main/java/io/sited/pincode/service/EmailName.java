package io.sited.pincode.service;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chi
 */
public class EmailName {
    public static final Pattern PATTERN = Pattern.compile("(.*)<(.*)>");
    public final String email;
    public final String username;

    public EmailName(String email) {
        Matcher matcher = PATTERN.matcher(email);
        if (matcher.matches()) {
            this.email = matcher.group(1);
            username = matcher.group(2);
        } else {
            this.email = email;
            username = null;
        }
    }

    public Address toAddress() throws AddressException, UnsupportedEncodingException {
        if (username == null) {
            return new InternetAddress(email);
        } else {
            return new InternetAddress(email, username);
        }
    }
}
