package rash.rest.components.emailProvider.impl;

import rash.rest.components.emailProvider.EmailProvider;
import rash.rest.entity.email.Email;

import javax.mail.*;
import java.util.Arrays;
import java.util.List;


public class EmailProviderFacade implements EmailProvider {
    private EmailProviderImpl emailProvider;

    public EmailProviderFacade(EmailAccount account) {
        emailProvider = EmailProviderImpl.builder()
                .buildAddress(account.getEmail())
                .buildAuthenticator(account.getEmail(), account.getPassword())
                .build();
    }

    @Override
    public Email send(Email email) {

        Message message = null;
        try {
            message = emailProvider.createEmptyOutputMessage();
            EmailUtils.outputMessageDataSetter(message, email);
            Transport.send(message);
        } catch (Exception e) {
            throw new RuntimeException( "email sending exception", e);
        }

        Email lastOutputEmail =  getOutput(0,1).get(0);

        return lastOutputEmail;
    }

    @Override
    public List<Email> getInput(int start, int count) {

        Folder folder = emailProvider.getFolder("inbox");

        List<Email> emails = null;
        try {
            emails = EmailUtils.getEmails(folder, start, count);
        } catch (MessagingException e) {
            throw new RuntimeException("getting messages exception", e);
        }

        return emails;
    }

    @Override
    public List<Email> getOutput(int start, int count) {

        Folder folder = emailProvider.getFolder("[Gmail]/Отправленные");

        List<Email> emails = null;
        try {
            emails = EmailUtils.getEmails(folder, start, count);
        } catch (MessagingException e) {
            throw new RuntimeException("getting messages exception", e);
        }

        return emails;
    }

    @Override
    public String getProviderEmail() {
        return emailProvider.getProviderAddress().getAddress();
    }

    @Override
    public void sout() {

        Folder folder = emailProvider.getFolder("[Gmail]");

        Folder[] folders = null;
        try {
            folders = folder.list();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Arrays.stream( folders )
                .map(Folder::getFullName)
                .map((folderName) -> {
                    System.out.println(folderName);

                    Folder folder1 = null;
                    try {
                        folder1 = emailProvider.getFolder(folderName);
                    } catch (Exception e) {
                        throw new RuntimeException();
                    }
                    return folder1;
                })
                .forEach((f) -> {

                    try {
                        System.out.println(f.getMessageCount());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                });

    }
}
