package io.sited.email;

import io.sited.email.api.email.SendEmailRequest;
import io.sited.email.api.email.SendEmailResponse;

/**
 * @author chi
 */
public interface EmailVendor {
    SendEmailResponse send(SendEmailRequest request);
}
