package com.vmoon.carx.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vmoon.carx.entities.UserEntity;
import com.vmoon.carx.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticatedUser {
    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    @Transactional
    public Optional<UserEntity> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userRepository.findByUsername(userDetails.getUsername()));
    }

    public void logout() {
        authenticationContext.logout();
    }

}
