package com.sportscenter.service.impl;

import com.sportscenter.model.entity.PasswordResetToken;
import com.sportscenter.model.entity.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@RequiredArgsConstructor
@Service
public class EmailService {

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;

    public void sendRegistrationEmail(String userEmail, String username) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setFrom("admin@sportscentergoranov.com");
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setSubject("Welcome!");
            mimeMessageHelper.setText(generateRegistrationMessageContent(username), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateRegistrationMessageContent(String username) {

        Context ctx = new Context();
        ctx.setVariable("username", username);

        return templateEngine.process("email/registration", ctx);
    }



    public void sendPasswordResetEmail(PasswordResetToken pwResetToken,
                                       String contextPath) {
        String url = contextPath + "/changePassword?token=" + pwResetToken.getToken();

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("admin@sportscentergoranov.com");
            mimeMessageHelper.setTo(pwResetToken.getUser().getEmail());
            mimeMessageHelper.setSubject("Password Change Request");
            mimeMessageHelper.setText(generatePasswordResetMessageContent(pwResetToken.getUser(), url), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }

    private String generatePasswordResetMessageContent(UserEntity user, String url) {
        Context ctx = new Context();
        ctx.setVariable("username", user.getUsername());
        ctx.setVariable("url", url);

        return templateEngine.process("email/change-password", ctx);
    }
}
