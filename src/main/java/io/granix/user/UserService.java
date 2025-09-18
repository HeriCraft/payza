package io.granix.user;

import io.granix.common.event.user.UserCreatedEvent;
import io.granix.common.security.CryptoService;
import io.granix.user.entity.Role;
import io.granix.user.entity.RoleNames;
import io.granix.user.entity.UserEntity;
import io.granix.user.entity.UserStatus;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class UserService {
    @Inject
    private UserRepository repository;

    @Inject
    private CryptoService cryptoService;

    @Inject
    Event<UserCreatedEvent> userCreated;

    public UserService(){ }

    public UserService(UserRepository repository, CryptoService cryptoService){
        this.repository = repository;
        this.cryptoService = cryptoService;
    }

    public UserService(UserRepository repository, CryptoService cryptoService, Event<UserCreatedEvent> event){
        this.repository = repository;
        this.cryptoService = cryptoService;
        this.userCreated = event;
    }

    /**
     * After the user registration, we should emit UserCreatedEvent
     * in order that others module, such as wallet and notification can do their jobs.
     */
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

        // Verify that msisdn doesn't exist in database
        if (repository.find("msisdnEncrypted", cryptoService.encrypt(msisdn)).firstResult() != null)
            throw new IllegalArgumentException("Phone number already in use");

        // Setup default Role
        var dafaultRole = new Role(RoleNames.USER);

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

        userCreated.fireAsync(new UserCreatedEvent(
                user.id,
                user.msisdnEncrypted,
                user.emailEncrypted
        ));

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
