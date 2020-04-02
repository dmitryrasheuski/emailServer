package rash.rest.components.emailProvider.impl;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailProviderImpl {
    private Session session;
    private InternetAddress providerAddress;
    private Store store;

    private EmailProviderImpl(InternetAddress providerAddress, Session session) {
        this.session = session;
        this.providerAddress = providerAddress;

        try {
            store = session.getStore();
            store.connect();
        } catch (MessagingException e) {
            throw new RuntimeException("store getting exception", e);
        }
    }
    public static Builder builder() {
        return new Builder();
    }

    public Folder getFolder(String folderName) {
        Folder folder = null;
        try {
            folder = store.getFolder(folderName);
            folder.open(Folder.READ_ONLY);
        } catch (MessagingException e) {
            throw new RuntimeException("getting folder exception", e);
        }
        return folder;
    }
    public Message createEmptyOutputMessage() throws MessagingException {

        Message message = new MimeMessage(session);
        message.setFrom(providerAddress);
        return message;
    }
    public InternetAddress getProviderAddress() {
        return providerAddress;
    }

    public static class Builder {
        private InternetAddress from;
        private Authenticator authenticator;

        public Builder buildAddress(String email) {

            try {
                from = new InternetAddress(email);
            } catch (AddressException e) {
                throw new IllegalArgumentException(email + " isn't internet address", e);
            }

            return this;
        }

        public Builder buildAuthenticator(String email, String password) {
            authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, password);
                }
            };

            return this;
        }

        public EmailProviderImpl build() {
            Session session = Session.getDefaultInstance( getProperties(), authenticator );

            return new EmailProviderImpl(from, session);
        }

        Properties getProperties() {
            Properties props = new Properties();
            //sender properties
            props.put( "mail.smtp.auth", true );
            props.put( "mail.smtp.starttls.enable", true );
            props.put( "mail.smtp.host", "smtp.gmail.com");
            props.put( "mail.smtp.port", 587 );
            //receiver properties
            props.put("mail.store.protocol", "imap");
            props.put("mail.imap.ssl.enable", "true");
            props.put("mail.imap.port", 993);
            props.put("mail.imap.host", "imap.gmail.com");

            return props;
        }
    }

}
