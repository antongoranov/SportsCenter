package com.sportscenter.repository;

import com.sportscenter.model.entity.PasswordResetToken;
import com.sportscenter.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(UserEntity user);
    void deleteByExpiryDateBefore(LocalDateTime currentDateTime);

}
