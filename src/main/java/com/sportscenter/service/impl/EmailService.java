package com.sportscenter.service.impl;

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
            mimeMessageHelper.setText(generateMessageContent(username), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private String generateMessageContent(String username) {

        //thymeleaf context
        Context ctx = new Context();

        ctx.setVariable("username", username);

        //returns the String representation of the template
        return templateEngine.process("email/registration", ctx);
    }

}
