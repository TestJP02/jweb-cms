package io.sited.email.smtp.service;

import io.sited.email.EmailVendor;
import io.sited.email.api.email.MimeType;
import io.sited.email.api.email.SendEmailRequest;
import io.sited.email.api.email.SendEmailResponse;
import io.sited.email.api.email.SendEmailStatus;
import io.sited.email.smtp.SMTPOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;


/**
 * @author chi
 */
public class SMTPEmailVendorImpl implements EmailVendor {
    private final Logger logger = LoggerFactory.getLogger(SMTPEmailVendorImpl.class);
    private final Session mailSession;

    public SMTPEmailVendorImpl(SMTPOptions options) {
        Properties properties = new Properties();
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", options.host);
        properties.setProperty("mail.smtp.port", options.port);
        properties.setProperty("mail.user", options.username);
        properties.setProperty("mail.password", options.password);
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.mime.charset", "UTF-8");
        if (options.starttls != null) {
            properties.put("mail.smtp.starttls.enable", options.starttls);
        }
        if (options.ssl != null) {
            properties.put("mail.smtp.ssl.enable", options.ssl);
        }
        properties.put("mail.smtp.socketFactory.port", options.port);
        properties.put("mail.smtp.ssl.socketFactory", "javax.net.ssl.SSLSocketFactory");

        mailSession = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
            }
        });
    }

    @Override
    public SendEmailResponse send(SendEmailRequest request) {
        try {
            Message message = new MimeMessage(mailSession);
            message.setSubject(request.subject);
            if (request.mimeType == MimeType.HTML) {
                message.setContent(request.content, "text/html;charset=UTF-8");
            } else {
                message.setText(request.content);
            }

            message.setFrom(new EmailName(mailSession.getProperty("mail.user")).toAddress());
            Transport transport = mailSession.getTransport();
            transport.connect();
            transport.sendMessage(message, addresses(request.to));
            transport.close();
            SendEmailResponse sendResponse = new SendEmailResponse();
            sendResponse.status = SendEmailStatus.SUCCESS;
            return sendResponse;
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("failed to send email, from={}, to={}, subject={}", request.from, request.to, request.subject, e);
            SendEmailResponse sendResponse = new SendEmailResponse();
            sendResponse.status = SendEmailStatus.FAILED;
            sendResponse.errorMessage = e.getMessage();
            return sendResponse;
        }
    }

    private Address[] addresses(List<String> to) throws AddressException, UnsupportedEncodingException {
        Address[] addresses = new Address[to.size()];
        for (int i = 0; i < to.size(); i++) {
            addresses[i] = new EmailName(to.get(i)).toAddress();
        }
        return addresses;
    }
}
