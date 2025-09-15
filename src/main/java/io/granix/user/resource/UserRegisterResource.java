package io.granix.user.resource;

import io.granix.user.dto.request.UserRegisterRequest;
import io.granix.user.dto.response.UserRegisterResponse;
import jakarta.resource.NotSupportedException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/user")
public class UserRegisterResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public UserRegisterResponse register(UserRegisterRequest request) throws NotSupportedException {
        throw new NotSupportedException();
    }
}
