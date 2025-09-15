package io.granix.user.entity;

import io.granix.common.converter.MapToJsonConverter;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "user")
public class UserEntity extends PanacheEntityBase {
    // System information
    @Id
    public UUID id;
    public Date creationDate;
    public boolean isRGPD;
    @Enumerated(EnumType.STRING)
    public UserStatus status;
    @Column(nullable = false)
    public String passwordHashed;

    //Personal information
    public String firstname;
    public String lastname;
    public String gender;
    @Column(nullable = false, unique = true)
    public String phoneNumberEncrypted;
    public String emailEncrypted;
    public Date birthday;
    @Enumerated(EnumType.STRING)
    public UserIDPieceType identityPieceType;
    public String identityNumber;
    @Convert(converter = MapToJsonConverter.class)
    public Map<String, Object> metadata;
}
