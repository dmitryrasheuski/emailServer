package rash.rest.components.emailProvider.impl;

import lombok.SneakyThrows;
import rash.rest.entity.email.Email;
import rash.rest.entity.email.EmailContent;
import rash.rest.entity.email.EmailItem;
import rash.rest.entity.email.EmailMetaData;
import rash.rest.entity.email.EmailPart;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class EmailUtils {

    public static InternetAddress getInternetAddress(String email) {

        InternetAddress address = null;
        try {
            address = new InternetAddress(email);
        } catch (AddressException e) {
            throw new IllegalArgumentException("email string parsing exception. email: " + email, e);
        }

        return address;
    }

//    public static Address[] getAddresses(Email email) {
//
//        List<String> emailRecipients = email.getMetaData().getEmailRecipients();
//
//        Address[] addresses= emailRecipients.stream()
//                .map(EmailUtils::getInternetAddress)
//                .toArray(Address[]::new);
//
//        return addresses;
//    }

    public static List<Email> getEmails(Folder folder, int start, int count) throws MessagingException {
        //мы предоставляем стандартный итерфейс индексирования массива(последнее сообщение под индексом 0, а первое -- (count-1))
        //а в библиотеке javax.mail последнее сообщение находится под индексом getMessageCount(), а первое -- 1
        //поэтому
        int endMappingIndex = folder.getMessageCount() - start;
        int startMappingIndex = endMappingIndex - count + 1;

        Message[] messages = folder.getMessages( startMappingIndex, endMappingIndex  );

        return Arrays.stream(messages)
                .map(EmailUtils::getEmail)
                .collect(Collectors.toList());
    }

    public static Email getEmail(Message message) {

        Email email = null;
        EmailMetaData metaData = null;
        EmailItem emailContent = null;
        try {
            metaData = buildEmailMetadata(message);
            emailContent = buildEmailContent(message);
            email = new Email(metaData, emailContent);
        } catch (Exception e) {
            throw new RuntimeException("parsing message exception", e);
        }

        return email;
    }

    public static EmailMetaData buildEmailMetadata(Message message) throws MessagingException {

        List<String> emailRecipients = Arrays.stream( message.getAllRecipients() )
                .map(Address::toString)
                .collect(Collectors.toList());

        return EmailMetaData.builder()
                    .subject( message.getSubject() )
                    .emailRecipients( emailRecipients )
                    .fromEmail( message.getFrom()[0].toString() )
                    .receivedDate(
                            new Timestamp( message.getReceivedDate().getTime() )
                    )
                    .sentDate(
                            new Timestamp( message.getReceivedDate().getTime() )
                    )
                    .build();
    }

    public static EmailItem buildEmailContent(Object obj) throws MessagingException, IOException {

        if (obj instanceof Multipart) {

            Multipart multipart = (Multipart) obj;
            String multipartType = multipart.getContentType();
            EmailPart emailPart = new EmailPart(multipartType, new ArrayList<EmailItem>());

            for (int i = 0; i < multipart.getCount() ; i++) {

                BodyPart bodyPart = multipart.getBodyPart(i);
                EmailItem emailItem = buildEmailContent(bodyPart);

                emailPart.addItem(emailItem);
            }

            return emailPart;

        } else if (obj instanceof Part) {

            Part part = (Part) obj;
            String contentType = part.getContentType();
            Object content = part.getContent();

            return (content instanceof Multipart) ?
                    buildEmailContent(content) :
                    new EmailContent(contentType, content);

//        } else if (obj instanceof BodyPart) {
//
//            BodyPart bodyPart = (BodyPart) obj;
//            String contentType = bodyPart.getContentType();
//            Object content = bodyPart.getContent();
//
//            return (content instanceof Multipart) ?
//                    buildEmailContent(content) :
//                    new EmailContent(contentType, content);
//
//        } else if (obj instanceof Message) {
//
//            Message message = (Message) obj;
//            String contentType = message.getContentType();
//            Object content = message.getContent();
//
//            return ( contentType.contains("multipart/") ) ?
//                    buildEmailContent(content) :
//                    new EmailContent(contentType, content);

        } else {//эта часть устовия является подстраховкой, как правило поток выполнения сюда не идет и не дожден заходить

            String contentType = obj.getClass().getCanonicalName();
            return new EmailContent(contentType, obj);
        }
    }

    public static void outputMessageDataSetter(Message message, Email email) throws MessagingException, IOException {

        BodyPart body = buildBodyPart( email.getContent() );
        Address[] emailRecipients = email.getMetaData().getEmailRecipients().stream()
                .map(EmailUtils::getInternetAddress)
                .toArray(Address[]::new);

        message.setSubject( email.getMetaData().getSubject() );
        message.setContent( body.getContent(), body.getContentType() );
        message.setRecipients(
                Message.RecipientType.TO,
                emailRecipients
        );
    }

    @SneakyThrows
    public static BodyPart buildBodyPart(EmailItem emailItem) {

        String contentType = emailItem.getContentType();
        Object content = emailItem.getContent();

        BodyPart bodyPart = new MimeBodyPart();
        if(emailItem instanceof EmailPart) {
            List<EmailItem> emailItems = (List<EmailItem>) content;
            BodyPart[] bodyParts = new BodyPart[ emailItems.size() ];
            for (int i = 0; i < emailItems.size(); i++) {
                bodyParts[i] = buildBodyPart(emailItems.get(i));
            }

            String subtype = contentType.toUpperCase().replace("MULTIPART/", "");
            bodyPart.setContent( new MimeMultipart(subtype, bodyParts) );

        } else {
            bodyPart.setContent(content, contentType);
        }

        return bodyPart;
    }

}
