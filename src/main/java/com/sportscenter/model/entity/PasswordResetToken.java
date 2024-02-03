package com.sportscenter.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken extends BaseEntity{

    @Column(nullable = false)
    private String token;

    @OneToOne(targetEntity = UserEntity.class, optional = false)
    private UserEntity user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

}
