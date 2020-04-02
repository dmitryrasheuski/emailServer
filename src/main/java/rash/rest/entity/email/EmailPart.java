package rash.rest.entity.email;

import lombok.AllArgsConstructor;
import rash.rest.entity.email.EmailItem;

import java.util.List;

@AllArgsConstructor
public class EmailPart implements EmailItem {
    private String contentType;
    private List<EmailItem> items;

    @Override
    public Object getContent() {
        return items;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    public boolean addItem(EmailItem item) {
        return items.add(item);
    }

    public boolean removeItem(EmailItem item) {
        return items.remove(item);
    }

}
