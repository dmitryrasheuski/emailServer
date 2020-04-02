package rash.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rash.rest.components.emailProvider.EmailProvider;
import rash.rest.components.emailProvider.impl.EmailAccount;
import rash.rest.components.emailProvider.impl.EmailProviderFacade;

@Configuration
public class EmailConfig {

    @Bean
    @Autowired
    public EmailAccount emailAccount(ApplicationArguments applicationArguments) {
        String[] args = applicationArguments.getSourceArgs();
        if (args == null || args.length < 2) throw new RuntimeException("Comandline argument exception");
        String email = args[0];
        String password = args[1];
        return new EmailAccount(email, password);
    }

    @Bean
    @Autowired
    public EmailProvider emailProvider(EmailAccount account) {
        return new EmailProviderFacade(account);
    }

}
