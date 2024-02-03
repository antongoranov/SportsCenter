package com.sportscenter.service.impl;

import com.sportscenter.exception.ObjectNotFoundException;
import com.sportscenter.model.entity.PasswordResetToken;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.repository.PasswordResetTokenRepository;
import com.sportscenter.service.PasswordResetTokenService;
import com.sportscenter.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final UserService userService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public PasswordResetToken createPasswordResetTokenForUser(String email) {

        //will always have valid user, as email presence is checked on endpoint level
        UserEntity user = userService.getUserByEmail(email);
        String token = UUID.randomUUID().toString();

        PasswordResetToken pwResetToken = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();

        return passwordResetTokenRepository.save(pwResetToken);
    }

    @Override
    public boolean userHasExistingToken(String email){
        UserEntity user = userService.getUserByEmail(email);
        return passwordResetTokenRepository
                .findByUser(user)
                .isPresent();
    }

    @Override
    public void deleteToken(PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.delete(passwordResetToken);
    }

    @Override
    public PasswordResetToken getTokenByUserEmail(String email) {
        UserEntity user = userService.getUserByEmail(email);

        return passwordResetTokenRepository
                .findByUser(user)
                .orElseThrow(() -> new ObjectNotFoundException("No token found for user with email: " + email));
    }

    @Override
    public boolean isTokenValid(String token) {
        PasswordResetToken passwordResetToken =
                passwordResetTokenRepository.findByToken(token).get();

        return passwordResetToken.getExpiryDate().isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isTokenPresent(String token){

        if(passwordResetTokenRepository.findByToken(token).isEmpty()) {
            throw new ObjectNotFoundException("Token " + token + " not found!");
        }

        return true;
    }

    //Run every SUN to delete expired tokens for the past week
    @Override
    @Transactional
    @Scheduled(cron = "0 59 23 ? * SUN")
    public void removeExpiredTokens() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        passwordResetTokenRepository.deleteByExpiryDateBefore(currentDateTime);
    }
}
