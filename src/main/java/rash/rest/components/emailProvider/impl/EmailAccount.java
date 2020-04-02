package rash.rest.components.emailProvider.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailAccount {
    private String email;
    private String password;
}
