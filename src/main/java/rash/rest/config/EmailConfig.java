package rash.rest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rash.rest.components.emailProvider.EmailProvider;
import rash.rest.components.emailProvider.impl.EmailAccount;
import rash.rest.components.emailProvider.impl.EmailProviderFacade;
import rash.rest.components.emailProvider.impl.EmailProviderImpl;

@Configuration
public class EmailConfig {

    @Bean
    public static EmailAccount emailAccount(
            @Value("${email.email}") String email,
            @Value("${email.password}") String password
    ) {
        return new EmailAccount(email, password);
    }

    @Bean
    @Autowired
    public static EmailProvider emailProvider(EmailAccount account) {
        return new EmailProviderFacade(account);
    }

}
