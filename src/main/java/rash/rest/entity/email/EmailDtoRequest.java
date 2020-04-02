package rash.rest.entity.email;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@JsonDeserialize(using = EmailDtoRequest.Deserializer.class)
public class EmailDtoRequest {
    private List<String> emailRecipients;
    private String subject;
    private EmailItem emailContent;

    public Email toEmail() {
        EmailMetaData emailMetaData = new EmailMetaData(emailRecipients, null, subject, null, null);
        return new Email(emailMetaData, emailContent);
    }

    public static class Deserializer extends JsonDeserializer<EmailDtoRequest> {

        @Override
        public EmailDtoRequest deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

            ObjectCodec oc = p.getCodec();
            JsonNode node = oc.readTree(p);

            List<String> emailReceivers = getReceiver( node.get("to") );
            String subject = node.get("subject").asText();
            EmailItem content = getEmailItem( node.get("emailContent") );

            return new EmailDtoRequest(emailReceivers, subject, content);
        }

        @SneakyThrows
        EmailItem getEmailItem(JsonNode node) {

            JsonNodeType nodeType = node.getNodeType();

            String contentType = node.get("contentType").asText().toUpperCase();
            JsonNode contentNode = node.get("content");

            EmailItem emailItem = null;
            if (contentNode.isContainerNode()) {

                List<EmailItem> items = new ArrayList<>();
                for (JsonNode subNode : contentNode) {
                    EmailItem item = getEmailItem(subNode);
                    items.add(item);
                }
                emailItem = new EmailPart(contentType, items);

            } else {

                Object content = null;
                if ( contentType.contains("TEXT/PLAIN") ) {

                    contentType =  contentType + "; charset=UTF-8";
                    content = contentNode.asText();

                } else if ( contentType.contains("TEXT/HTML") ) {

                    contentType = contentType + "; charset=UTF-8";
                    byte[] binaryValue = contentNode.binaryValue();
                    content = new String(binaryValue, StandardCharsets.UTF_8);

                } else {

                    String fileName = node.get("fileName").asText();
                    contentType =  contentType + "; name=" + fileName;
                    content = contentNode.binaryValue();

                }

                emailItem = new EmailContent(contentType, content);
            }

            return emailItem;
        }


        List<String> getReceiver(JsonNode node) {
            ArrayList<String> list = new ArrayList<>();

            if( node.isValueNode() ) {

                String receiver = node.asText();
                list.add(receiver);

            } else {
                //TODO
            }

            return list;
        }
    }
}

