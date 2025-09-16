package io.granix.user.dto.request;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthenticationRequest {
    public String msisdn;
    public String password;
}
