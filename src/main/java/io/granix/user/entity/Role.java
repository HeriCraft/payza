package io.granix.user.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "pz_role")
public class Role extends PanacheEntity {
    @Enumerated(EnumType.STRING)
    public RoleNames name;

    public Role(){ }

    public Role(RoleNames name) {
        this.name = name;
    }
}
