package rash.rest.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rash.rest.components.emailProvider.EmailProvider;
import rash.rest.entity.email.EmailDtoRequest;
import rash.rest.entity.email.Email;
import rash.rest.services.EmailService;

import java.util.List;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    private EmailProvider emailProvider;

    @Autowired
    public EmailServiceImpl(EmailProvider emailProvider) {
        this.emailProvider = emailProvider;
    }

    @Override
    public Email sendEmail(EmailDtoRequest dto) {
        Email requestEmail = dto.toEmail();
        Email responseEmail = emailProvider.send(requestEmail);

        return responseEmail;
    }

    @Override
    public Email getOutputEmail(int index) {
        List<Email> list = emailProvider.getOutput(index,1);

        return list.get(0);
    }

    @Override
    public Email getInputEmail(int index) {
        List<Email> list = emailProvider.getInput(index,1);

        return list.get(0);
    }

    @Override
    public void sout() {
        emailProvider.sout();
    }
}
