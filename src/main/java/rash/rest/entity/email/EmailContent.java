package rash.rest.entity.email;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EmailContent implements EmailItem {
    private String contentType;
    private Object content;

    @Override
    public Object getContent() {
        return content;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

}
