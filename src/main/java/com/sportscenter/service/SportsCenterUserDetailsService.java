package com.sportscenter.service;

import com.sportscenter.model.entity.UserEntity;
import com.sportscenter.model.entity.UserRoleEntity;
import com.sportscenter.repository.UserRepository;
import com.sportscenter.model.security.SportsCenterUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class SportsCenterUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByUsername(username)
                .map(this::mapToSecurityUser)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + username + " not found!"));
    }

    private SportsCenterUserDetails mapToSecurityUser(UserEntity userEntity) {

        return SportsCenterUserDetails.builder()
                    .firstName(userEntity.getFirstName())
                    .lastName(userEntity.getLastName())
                    .email(userEntity.getEmail())
                    .username(userEntity.getUsername())
                    .password(userEntity.getPassword())
                    .profilePictureUrl(userEntity.getProfilePictureUrl())
                    .authorities(userEntity.getRoles()
                            .stream()
                            .map(this::mapToGrantedAuthority)
                            .toList())

                .build();
    }

    private GrantedAuthority mapToGrantedAuthority(UserRoleEntity role) {
        return new SimpleGrantedAuthority("ROLE_" + role.getRole().name());
    }
}
