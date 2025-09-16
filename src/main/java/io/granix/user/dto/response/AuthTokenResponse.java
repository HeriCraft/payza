package io.granix.user.dto.response;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@ApplicationScoped
public class AuthTokenResponse {
    public String token;

    public AuthTokenResponse(String token) {
        this.token = token;
    }
    public AuthTokenResponse(){

    }
}
