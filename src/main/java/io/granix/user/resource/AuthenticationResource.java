package io.granix.user.resource;

import io.granix.user.UserService;
import io.granix.user.dto.request.UserAuthenticationRequest;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/user")
public class AuthenticationResource {

    @Inject
    UserService service;

    @POST
    @Path("/auth/token")
    public Map<String, Object> authenticationToken(UserAuthenticationRequest request)
    {
        var response = new HashMap<String, Object>();
        try {
            var user = service.authenticate(
                    request.msisdn,
                    request.password
            );

            System.out.println("MSISDN: " + service.decryptPhone(user));

            var groups = user.roles.stream()
                    .map(r -> r.name.name()) // Transforme enum -> String (e.g. "ADMIN")
                    .collect(Collectors.toSet());

            String token = Jwt.issuer("granix-payza")
                    .upn(service.decryptPhone(user))
                    .groups(groups)
                    .claim("userId", user.id.toString())
                    .expiresIn(3600)
                    .sign();

            response.put("token", token);
            response.put("expiration", 3600);
            response.put("user_id", user.id);

            return response;
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            response.put("status", "Error");
            response.put("message", e.getMessage());

            return response;
        }
    }
}
