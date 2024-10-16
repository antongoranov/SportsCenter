package com.sportscenter.service;

import com.sportscenter.model.entity.PasswordResetToken;
import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.service.impl.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSenderMock;
    @Mock
    private TemplateEngine templateEngineMock;

    @InjectMocks
    private EmailService emailServiceTest;

    @Test
    public void testSendRegistrationEmail(){
        when(javaMailSenderMock.createMimeMessage())
                .thenReturn(mock(MimeMessage.class));

        when(templateEngineMock.process(any(String.class), any(Context.class)))
                .thenReturn("");

        emailServiceTest.sendRegistrationEmail("email", "testUsername");

        verify(javaMailSenderMock, times(1))
                .send(any(MimeMessage.class));
    }

    @Test
    public void testSendPasswordResetEmail(){
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token("testToken")
                .user(UserEntity.builder()
                        .email("test@email.com")
                        .build())
                .build();

        when(javaMailSenderMock.createMimeMessage())
                .thenReturn(mock(MimeMessage.class));

        when(templateEngineMock.process(any(String.class), any(Context.class)))
                .thenReturn("");

        emailServiceTest.sendPasswordResetEmail(passwordResetToken, "path");

        verify(javaMailSenderMock, times(1))
                .send(any(MimeMessage.class));

    }
}
