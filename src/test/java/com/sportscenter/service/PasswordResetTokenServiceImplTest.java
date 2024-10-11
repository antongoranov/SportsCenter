package com.sportscenter.service;

import com.sportscenter.exception.ObjectNotFoundException;
import com.sportscenter.model.entity.PasswordResetToken;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.repository.PasswordResetTokenRepository;
import com.sportscenter.service.impl.PasswordResetTokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetTokenServiceImplTest {

    @Mock
    private UserService userServiceMock;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepositoryMock;

    @InjectMocks
    private PasswordResetTokenServiceImpl passwordResetTokenServiceTest;

    //createPasswordResetTokenForUser
    @Test
    public void testCreatePasswordResetTokenForUser_createsToken(){
        String emailTest = "test@test.com";
        UserEntity userEntityTest = mock(UserEntity.class);

        when(userServiceMock.getUserByEmail(emailTest))
                .thenReturn(userEntityTest);

        passwordResetTokenServiceTest.createPasswordResetTokenForUser(emailTest);

        verify(passwordResetTokenRepositoryMock, times(1))
                .save(any(PasswordResetToken.class));
    }

    //userHasExistingToken
    @Test
    public void testUserHasExistingToken_true(){
        String emailTest = "test@test.com";
        UserEntity userEntityTest = mock(UserEntity.class);

        when(userServiceMock.getUserByEmail(emailTest))
                .thenReturn(userEntityTest);

        when(passwordResetTokenRepositoryMock.findByUser(userEntityTest))
                .thenReturn(Optional.of(new PasswordResetToken()));

        boolean actual = passwordResetTokenServiceTest.userHasExistingToken(emailTest);

        assertTrue(actual);
    }

    @Test
    public void testUserHasExistingToken_false(){
        String emailTest = "test@test.com";
        UserEntity userEntityTest = mock(UserEntity.class);

        when(userServiceMock.getUserByEmail(emailTest))
                .thenReturn(userEntityTest);

        when(passwordResetTokenRepositoryMock.findByUser(userEntityTest))
                .thenReturn(Optional.empty());

        boolean actual = passwordResetTokenServiceTest.userHasExistingToken(emailTest);

        assertFalse(actual);
    }

    //deleteToken
    @Test
    public void testDeleteToken_deletesToken(){
        PasswordResetToken passwordResetTokenTest = mock(PasswordResetToken.class);

        passwordResetTokenServiceTest.deleteToken(passwordResetTokenTest);

        verify(passwordResetTokenRepositoryMock, times(1))
                .delete(passwordResetTokenTest);
    }

    //getTokenByUserEmail
    @Test
    public void testGetTokenByUserEmail_returnsToken(){
        String emailTest = "test@test.com";
        UserEntity userEntityTest = mock(UserEntity.class);
        PasswordResetToken passwordResetTokenTest = new PasswordResetToken();

        when(userServiceMock.getUserByEmail(emailTest))
                .thenReturn(userEntityTest);
        when(passwordResetTokenRepositoryMock.findByUser(userEntityTest))
                .thenReturn(Optional.of(passwordResetTokenTest));

        PasswordResetToken actual = passwordResetTokenServiceTest.getTokenByUserEmail(emailTest);

        assertEquals(passwordResetTokenTest, actual);
    }

    @Test
    public void testGetTokenByUserEmail_throws(){
        String emailTest = "test@test.com";
        UserEntity userEntityTest = mock(UserEntity.class);

        when(userServiceMock.getUserByEmail(emailTest))
                .thenReturn(userEntityTest);
        when(passwordResetTokenRepositoryMock.findByUser(userEntityTest))
                .thenReturn(Optional.empty());

       assertThrows(ObjectNotFoundException.class,
               () -> passwordResetTokenServiceTest.getTokenByUserEmail(emailTest));
    }

    //isTokenValid
    @Test
    public void testIsTokenValid_isValid(){
        String tokenTest = "token";
        PasswordResetToken passwordResetTokenTest = PasswordResetToken.builder()
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();
        when(passwordResetTokenRepositoryMock.findByToken(tokenTest))
                .thenReturn(Optional.of(passwordResetTokenTest));

        assertTrue(passwordResetTokenServiceTest.isTokenValid(tokenTest));
    }

    @Test
    public void testIsTokenValid_isNotValid(){
        String tokenTest = "token";
        PasswordResetToken passwordResetTokenTest = PasswordResetToken.builder()
                .expiryDate(LocalDateTime.now())
                .build();

        when(passwordResetTokenRepositoryMock.findByToken(tokenTest))
                .thenReturn(Optional.of(passwordResetTokenTest));

        assertFalse(passwordResetTokenServiceTest.isTokenValid(tokenTest));
    }

    //isTokenPresent
    @Test
    public void testIsTokenPresent_isPresent(){
        String tokenTest = "token";
        PasswordResetToken passwordResetTokenTest = mock(PasswordResetToken.class);

        when(passwordResetTokenRepositoryMock.findByToken(tokenTest))
                .thenReturn(Optional.of(passwordResetTokenTest));

        assertTrue(passwordResetTokenServiceTest.isTokenPresent(tokenTest));
    }

    @Test
    public void testIsTokenPresent_isNotPresentAndThrows(){
        String tokenTest = "token";

        when(passwordResetTokenRepositoryMock.findByToken(tokenTest))
                .thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                () -> passwordResetTokenServiceTest.isTokenPresent(tokenTest));
    }

    //removeExpiredTokens
    @Test
    public void testRemoveExpiredTokens(){

        passwordResetTokenServiceTest.removeExpiredTokens();

        verify(passwordResetTokenRepositoryMock, times(1))
                .deleteByExpiryDateBefore(any(LocalDateTime.class));
    }
}
