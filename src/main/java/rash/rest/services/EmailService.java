package rash.rest.services;

import rash.rest.entity.email.EmailDtoRequest;
import rash.rest.entity.email.Email;

public interface EmailService {

    Email sendEmail(EmailDtoRequest dto);
    Email getOutputEmail(int index);
    Email getInputEmail(int index);
    void sout();
}
