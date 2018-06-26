package io.sited.email.smtp;

import io.sited.email.EmailModuleImpl;
import io.sited.email.EmailVendor;
import io.sited.email.smtp.service.SMTPEmailVendorImpl;

/**
 * @author chi
 */
public class SMTPModule extends EmailModuleImpl {
    @Override
    protected EmailVendor vendor() {
        SMTPOptions options = options("smtp", SMTPOptions.class);
        return new SMTPEmailVendorImpl(options);
    }
}
