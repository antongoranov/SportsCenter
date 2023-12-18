package com.sportscenter.serviceImpl;

import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.entity.UserRoleEntity;
import com.sportscenter.model.enums.UserRoleEnum;
import com.sportscenter.repository.UserRepository;
import com.sportscenter.service.impl.SportsCenterUserDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SportsCenterUserDetailsServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    @InjectMocks
    private SportsCenterUserDetailsService sportsCenterUserDetailsServiceTest;

    private UserEntity admin;

    @BeforeEach
    public void setUp(){

        sportsCenterUserDetailsServiceTest = new SportsCenterUserDetailsService(userRepositoryMock);

        UserRoleEntity adminRole = UserRoleEntity.builder()
                .role(UserRoleEnum.ADMIN)
                .build();

        UserRoleEntity userRole = UserRoleEntity.builder()
                .role(UserRoleEnum.USER)
                .build();

        admin = UserEntity.builder()
                .firstName("fn test")
                .lastName("ln test")
                .username("test")
                .email("test@email.com")
                .password("12345")
                .profilePictureUrl("url/test")
                .roles(Set.of(adminRole, userRole))
                .build();

    }

    @Test
    public void testLoadUserByUsernameReturnsUserDetails(){
        //Arrange
        when(userRepositoryMock.findByUsername(admin.getUsername()))
                .thenReturn(Optional.of(admin));

        //Act
        UserDetails actual = sportsCenterUserDetailsServiceTest
                .loadUserByUsername(admin.getUsername());

        //Assert
        Collection<? extends GrantedAuthority> authorities = actual.getAuthorities();
        Assertions.assertEquals(2, authorities.size());
        assertEquals(admin.getUsername(), actual.getUsername());

        boolean containsAdminRole = authorities
                .stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_" + UserRoleEnum.ADMIN.name()));

        boolean containsUserRole = authorities
                .stream()
                .anyMatch(grantedAuthority ->
                        grantedAuthority.getAuthority().equals("ROLE_" + UserRoleEnum.USER.name()));

        assertTrue(containsAdminRole);
        assertTrue(containsUserRole);
    }


    @Test
    public void testLoadUserByUsernameThrows(){

        assertThrows(UsernameNotFoundException.class,
                () -> sportsCenterUserDetailsServiceTest.loadUserByUsername("nonExistent"));

    }
}
