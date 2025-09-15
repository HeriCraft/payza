package io.granix.user;

import io.granix.user.entity.UserEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class UserRepository implements PanacheRepository<UserEntity> {
}
