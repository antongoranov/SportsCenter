package com.sportscenter.service;

import com.sportscenter.model.entity.PasswordResetToken;
import org.springframework.scheduling.annotation.Scheduled;

public interface PasswordResetTokenService {
    PasswordResetToken createPasswordResetTokenForUser(String email);

    boolean isTokenValid(String token);

    boolean isTokenPresent(String token);

    boolean userHasExistingToken(String email);

    PasswordResetToken getTokenByUserEmail(String email);

    void deleteToken(PasswordResetToken passwordResetToken);

    void removeExpiredTokens();
}
