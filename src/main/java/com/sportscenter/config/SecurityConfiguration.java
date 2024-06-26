package com.sportscenter.config;

import com.sportscenter.repository.UserRepository;
import com.sportscenter.service.impl.SportsCenterUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationSuccessHandler oAuthSuccessHandler) throws Exception {

        return http
                .authorizeHttpRequests(
                        authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                        .requestMatchers("/users/login", "/users/register", "/users/login-error").anonymous()
                                        .requestMatchers("/", "/maintenance", "/error",
                                                                  "/resetPassword", "/changePassword",
                                                                  "/sportClasses/all", "/instructors/**").permitAll()
                                        .anyRequest()
                                        .authenticated())
                .formLogin(
                        formLogin ->
                                formLogin.loginPage("/users/login")
                                        .usernameParameter("username")
                                        .passwordParameter("password")
                                        .defaultSuccessUrl("/", true)
                                        .failureForwardUrl("/users/login-error"))
                .oauth2Login(oauth2login -> oauth2login
                        .loginPage("/users/login")
                        .successHandler(oAuthSuccessHandler))
                .logout(
                        logout -> logout
                                .logoutUrl("/users/logout")
                                .logoutSuccessUrl("/")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID"))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new SportsCenterUserDetailsService(userRepository);
    }
}
