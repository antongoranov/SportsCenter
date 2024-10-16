package com.sportscenter.service;

import com.sportscenter.service.impl.OAuthSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OAuthSuccessHandlerTest {

    @Mock
    private UserService userServiceMock;

    private HttpServletRequest requestMock;
    private HttpServletResponse responseMock;
    private Authentication authenticationMock;

    @InjectMocks
    private OAuthSuccessHandler oAuthSuccessHandlerTest;

    @BeforeEach
    public void setUp() {
        requestMock = mock(HttpServletRequest.class);
        responseMock = mock(HttpServletResponse.class);
        authenticationMock = mock(Authentication.class);
    }

    @Test
    public void testOnAuthenticationSuccess_authenticates() throws ServletException, IOException {
        String testEmail = "test@email.com";
        DefaultOAuth2User userDetailsTest = mock(DefaultOAuth2User.class);

        when(authenticationMock.getPrincipal()).thenReturn(userDetailsTest);
        when(userDetailsTest.getAttribute("email"))
                .thenReturn(testEmail);

        oAuthSuccessHandlerTest.onAuthenticationSuccess(requestMock, responseMock, authenticationMock);

        verify(userServiceMock, times(1)).createUserIfNotExist(testEmail);
        verify(userServiceMock, times(1)).login(testEmail);
    }

    @Test
    public void testOnAuthenticationSuccess_doesNotAuthenticate() throws ServletException, IOException {
        when(authenticationMock.getPrincipal()).thenReturn(mock(Object.class));

        oAuthSuccessHandlerTest.onAuthenticationSuccess(requestMock, responseMock, authenticationMock);

        verify(userServiceMock, never()).createUserIfNotExist(anyString());
        verify(userServiceMock, never()).login(anyString());
    }


}
