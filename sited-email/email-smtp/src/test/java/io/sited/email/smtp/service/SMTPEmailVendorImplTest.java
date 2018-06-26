package io.sited.email.smtp.service;

import com.google.common.collect.Lists;
import io.sited.email.api.email.SendEmailRequest;
import io.sited.email.smtp.SMTPOptions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author chi
 */
@Disabled
public class SMTPEmailVendorImplTest {
    @Test
    public void send() {
        SMTPOptions smtpOptions = new SMTPOptions();
        smtpOptions.host = "smtp.qq.com";
        smtpOptions.port = "587";
        smtpOptions.username = "3262866804@qq.com";
        smtpOptions.password = "ebtutaqbvcsecjfd";

        SMTPEmailVendorImpl emailClient = new SMTPEmailVendorImpl(smtpOptions);
        SendEmailRequest request = new SendEmailRequest();
        request.from = "3262866804@qq.com";
        request.to = Lists.newArrayList("chiron.chi@gmail.com");
        request.subject = "test";
        request.content = "test";
        emailClient.send(request);
    }
}