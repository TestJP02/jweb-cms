package app.jweb.pincode.service;

import app.jweb.pincode.PinCodeOptions;
import app.jweb.ApplicationException;
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
import java.util.Properties;

/**
 * @author chi
 */
public class EmailSender {
    private final Logger logger = LoggerFactory.getLogger(EmailSender.class);
    private final Session session;

    public EmailSender(PinCodeOptions.SMTPOptions smtpOptions) {
        if (smtpOptions != null) {
            Properties properties = new Properties();
            properties.setProperty("mail.user", smtpOptions.username);
            properties.setProperty("mail.password", smtpOptions.password);
            properties.setProperty("mail.debug", "true");
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.host", smtpOptions.host);
            properties.setProperty("mail.smtp.port", String.valueOf(smtpOptions.port));
            properties.setProperty("mail.transport.protocol", "smtp");
            properties.setProperty("mail.mime.charset", "UTF-8");
            properties.setProperty("mail.replyTo", smtpOptions.replyTo);
            if (smtpOptions.starttlsEnabled != null) {
                properties.put("mail.smtp.starttls.enable", smtpOptions.starttlsEnabled);
            }
            if (smtpOptions.sslEnabled != null) {
                properties.put("mail.smtp.ssl.enable", smtpOptions.sslEnabled);
            }
            properties.put("mail.smtp.socketFactory.port", smtpOptions.port);
            properties.put("mail.smtp.ssl.socketFactory", "javax.net.ssl.SSLSocketFactory");

            this.session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
                }
            });
        } else {
            this.session = null;
        }
    }

    public void send(SendEmailRequest sendEmailRequest) {
        try {
            Session session = session();
            Message message = new MimeMessage(session);
            message.setSubject(sendEmailRequest.subject);
            if (sendEmailRequest.mimeType == MimeType.HTML) {
                message.setContent(sendEmailRequest.content, "text/html;charset=UTF-8");
            } else {
                message.setText(sendEmailRequest.content);
            }

            message.setFrom(new EmailName(session.getProperty("mail.user")).toAddress());
            message.setReplyTo(new Address[]{new EmailName(this.session.getProperty("mail.replyTo")).toAddress()});
            Transport transport = session.getTransport();
            transport.connect();
            transport.sendMessage(message, addresses(sendEmailRequest.to));
            transport.close();
        } catch (MessagingException | UnsupportedEncodingException e) {
            logger.error("failed to send email, to={}, subject={}", sendEmailRequest.to, sendEmailRequest.subject, e);
        }
    }

    private Session session() {
        if (session == null) {
            throw new ApplicationException("pincode smtp is not configured");
        }
        return session;
    }

    private Address[] addresses(String to) throws AddressException, UnsupportedEncodingException {
        Address[] addresses = new Address[1];
        addresses[0] = new EmailName(to).toAddress();
        return addresses;
    }
}
