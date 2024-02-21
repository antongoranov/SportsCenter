package com.sportscenter.service.impl;

import com.sportscenter.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if(authentication.getPrincipal() instanceof DefaultOAuth2User userDetails){

            String email = userDetails.getAttribute("email");

            userService.createUserIfNotExist(email);
            userService.login(email);

        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/");
    }
}
