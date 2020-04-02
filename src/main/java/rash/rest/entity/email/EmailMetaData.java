package rash.rest.entity.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailMetaData {

    private List<String> emailRecipients;
    private String fromEmail;
    private String subject;
    private Timestamp sentDate;
    private Timestamp receivedDate;
}
