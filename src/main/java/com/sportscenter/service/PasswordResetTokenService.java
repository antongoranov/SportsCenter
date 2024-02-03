package com.sportscenter.service;

import com.sportscenter.model.entity.PasswordResetToken;
import org.springframework.scheduling.annotation.Scheduled;

public interface PasswordResetTokenService {
    PasswordResetToken createPasswordResetTokenForUser(String email);

    boolean isTokenValid(String token);

    boolean isTokenPresent(String token);

//    boolean userHasExistingTokenAndValid(String email);

    boolean userHasExistingToken(String email);

    PasswordResetToken getTokenByUserEmail(String email);

    void deleteToken(PasswordResetToken passwordResetToken);

    //TODO: make scheduled task to delete expired tokens
    @Scheduled(cron = "0 35 13 * * ?") // Run daily at 2 AM
    void removeExpiredTokens();
}
