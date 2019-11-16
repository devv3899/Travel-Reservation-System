package com.flights.helper;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailHelper {

    public static void sendHtmlEmail(String subject, String htmlBodyContent, String textBodyContent,
                              String fromEmailAddress, String toEmailAddress) throws MessagingException {
        Session session = createMailSession();
        MimeMessage msg = createHtmlMessage(session, fromEmailAddress, toEmailAddress, subject, htmlBodyContent, textBodyContent);
        sendEmailMessage(session, msg);
    }

    private static Session createMailSession() {
        Properties props = System.getProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.port", 25);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");

        // Create a Session object to represent a mail session with the specified properties.
        return Session.getDefaultInstance(props);
    }

    private static MimeMessage createHtmlMessage(Session session, String fromEmailAddress, String recipientEmailAddress,
                                          String subject, String htmlBodyContent, String textBodyContent) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromEmailAddress));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmailAddress));
        msg.setSubject(subject);

        Multipart multiPart = new MimeMultipart("alternative");

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(textBodyContent, "utf-8");

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBodyContent, "text/html; charset=utf-8");

        // order of adding body parts into multiPart is important
        multiPart.addBodyPart(textPart);
        multiPart.addBodyPart(htmlPart);
        msg.setContent(multiPart);
        return msg;
    }

    private static void sendEmailMessage(Session session, MimeMessage msg) throws MessagingException {
        Transport transport = session.getTransport();

        try {
            transport.connect("email-smtp.us-east-1.amazonaws.com", "AKIAWVB2UCUMXR34MZOL", "BHy9FS+EgUjvmyo9gwtbXZQCia6Pcwx/c1qBt3XngHoE");
            transport.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Email has been sent!");
        } catch (MessagingException ex) {
        	 System.out.println("Email was not sent. Error message: " + ex.getMessage());
            throw ex;
        } finally {
            transport.close();
        }
    }
}
