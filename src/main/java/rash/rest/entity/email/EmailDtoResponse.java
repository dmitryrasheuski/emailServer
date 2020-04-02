package rash.rest.entity.email;

import lombok.Data;
import lombok.Getter;
import rash.rest.entity.email.Email;
import rash.rest.entity.email.EmailItem;
import rash.rest.entity.email.EmailPart;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class EmailDtoResponse {
    private List<String> emailRecipients;
    private String fromEmail;
    private String subject;
    private Timestamp receiveDate;
    private Timestamp sentDate;
    private EmailDtoContentType contentTypes;

    public EmailDtoResponse(Email email) {
        this.emailRecipients = email.getMetaData().getEmailRecipients();
        this.fromEmail = email.getMetaData().getFromEmail();
        this.subject = email.getMetaData().getSubject();
        this.receiveDate = email.getMetaData().getReceivedDate();
        this.sentDate = email.getMetaData().getSentDate();
        this.contentTypes = new EmailDtoContentType( email.getContent() );
    }

    @Getter
    private static class EmailDtoContentType {
        private String type;
        private List<EmailDtoContentType> subtypes;

        public EmailDtoContentType(EmailItem content) {
            type = content.getContentType();
            subtypes = new ArrayList<>();

            if(content instanceof EmailPart) {
                List<EmailItem> items = (List<EmailItem>) content.getContent();
                items.stream()
                        .map(EmailDtoContentType::new)
                        .forEach(subtypes::add);
            }

        }

    }
}
