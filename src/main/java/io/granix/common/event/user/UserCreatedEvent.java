package io.granix.common.event.user;

import java.util.UUID;

public class UserCreatedEvent {
    private final UUID userId;
    private final String msisdnEncrypted;
    private final String emailEncrypted;

    public UserCreatedEvent(
            UUID userId,
            String msisdnEncrypted,
            String emailEncrypted
    ) {
        this.userId = userId;
        this.msisdnEncrypted = msisdnEncrypted;
        this.emailEncrypted = emailEncrypted;
    }

    public UUID getUserId(){
        return this.userId;
    }

    public String getMsisdnEncrypted() {
        return this.msisdnEncrypted;
    }

    public String getEmailEncrypted() {
        return this.emailEncrypted;
    }
}