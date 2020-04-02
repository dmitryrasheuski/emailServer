package rash.rest.components.emailProvider;

import rash.rest.entity.email.Email;

import java.util.List;

public interface EmailProvider {
    Email send(Email email);
    List<Email> getInput(int start, int count);
    List<Email> getOutput(int start, int count);
    String getProviderEmail();
    void sout();
}
