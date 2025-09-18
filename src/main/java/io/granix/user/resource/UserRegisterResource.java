package io.granix.user.resource;

import io.granix.user.UserService;
import io.granix.user.dto.request.UserRegisterRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Path("/user")
public class UserRegisterResource {

    @Inject
    UserService service;

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> register(UserRegisterRequest request) {
        var response = new HashMap<String, Object>();
        try {
            var user = service.register(
                    request.msisdn,
                    request.password,
                    request.RGPDOption
            );
            System.out.println("User registered: "+user.toString());

            response.put("message", "User Registered Successfully!");
            response.put("userId", user.id);

            return response;
        } catch (InvalidAlgorithmParameterException
                 | InvalidKeyException
                 | NoSuchPaddingException
                 | IllegalBlockSizeException
                 | NoSuchAlgorithmException
                 | BadPaddingException
                 | IllegalArgumentException e) {
            System.out.println("Error during registration: " + e.getMessage());

            response.put("message", e.getMessage());
            return response;
        }
    }
}
