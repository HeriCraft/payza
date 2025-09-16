package io.granix.user;

import io.granix.common.security.CryptoService;
import io.granix.user.entity.Role;
import io.granix.user.entity.RoleNames;
import io.granix.user.entity.UserEntity;
import io.granix.user.entity.UserStatus;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ApplicationScoped
public class UserService {
    @Inject
    private UserRepository repository;

    @Inject
    private CryptoService cryptoService;

    @Transactional
    public UserEntity register(
            String msisdn,
            String password,
            boolean isRGPD
    ) throws InvalidAlgorithmParameterException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            BadPaddingException,
            InvalidKeyException {
        // Vérifier unicité
        if (repository.find("msisdnEncrypted", cryptoService.encrypt(msisdn)).firstResult() != null)
            throw new IllegalArgumentException("Phone number already in use");

        // Setup default Role
        var dafaultRole = new Role();
        dafaultRole.name = RoleNames.USER;

        // Setup User
        var user = new UserEntity();
        user.id = UUID.randomUUID();
        user.msisdnEncrypted = cryptoService.encrypt(msisdn);
        user.passwordHashed = BcryptUtil.bcryptHash(password);
        user.createdAt = LocalDateTime.now();
        user.isRGPD = isRGPD;
        user.status = UserStatus.ACTIVE;
        user.roles.add(dafaultRole);

        repository.persist(user);
        return user;
    }

    public UserEntity authenticate(String phone, String password)
            throws InvalidAlgorithmParameterException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            BadPaddingException,
            InvalidKeyException {
        String encryptedPhone = cryptoService.encrypt(phone);
        UserEntity user = repository.find("msisdnEncrypted", encryptedPhone).firstResult();

        if (user == null)
            throw new IllegalArgumentException("User not found");

        if (!BcryptUtil.matches(password, user.passwordHashed))
            throw new IllegalArgumentException("Invalid password");

        return user;
    }

    public String decryptPhone(UserEntity user)
            throws InvalidAlgorithmParameterException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            BadPaddingException,
            InvalidKeyException {
        return cryptoService.decrypt(user.msisdnEncrypted);
    }

    public String decryptEmail(UserEntity user)
            throws InvalidAlgorithmParameterException,
            NoSuchPaddingException,
            IllegalBlockSizeException,
            NoSuchAlgorithmException,
            BadPaddingException,
            InvalidKeyException {
        return cryptoService.decrypt(user.emailEncrypted);
    }
}
