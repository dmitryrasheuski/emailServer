package rash.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rash.rest.entity.email.EmailDtoRequest;
import rash.rest.entity.email.EmailDtoResponse;
import rash.rest.entity.email.Email;
import rash.rest.services.EmailService;


@RestController
@RequestMapping("/email")
public class EmailController {
    private EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/")
    public ResponseEntity<EmailDtoResponse> sendEmail( @RequestBody EmailDtoRequest emailDtoRequest) {
        Email email = emailService.sendEmail(emailDtoRequest);
        EmailDtoResponse dto = new EmailDtoResponse(email);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/output/{index}")
    public ResponseEntity<EmailDtoResponse> getOutputEmail(@PathVariable("index") int index ) {
        Email email = emailService.getOutputEmail(index);
        EmailDtoResponse dto = new EmailDtoResponse(email);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/input/{index}")
    public ResponseEntity<EmailDtoResponse> getInputEmail(@PathVariable("index") int index ) {
        Email email = emailService.getInputEmail(index);
        EmailDtoResponse dto = new EmailDtoResponse(email);
        return ResponseEntity.ok(dto);
    }

}
