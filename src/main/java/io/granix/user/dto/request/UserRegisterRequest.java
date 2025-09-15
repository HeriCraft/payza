package io.granix.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRegisterRequest {
    private String phoneNumber;
    private String password;
    private boolean RGPDOption;
}
