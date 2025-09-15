package io.granix.user;

import io.granix.user.entity.UserEntity;
import jakarta.inject.Inject;

import javax.naming.OperationNotSupportedException;

public class UserService {
    @Inject
    private UserRepository repository;

    public UserEntity register(
            String firstname,
            String lastname,
            String phoneNumber,
            String email,
            String password
    ) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Features not supported");
    }
}
