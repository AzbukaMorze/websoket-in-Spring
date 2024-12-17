package com.golovkin.websocket.service;

import com.golovkin.websocket.model.CustomUserDetails;
import com.golovkin.websocket.model.User;
import com.golovkin.websocket.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> authUser = userRepository.findByUsername(username.toLowerCase());
        if (authUser.isEmpty()) {
            throw new UsernameNotFoundException(username);
        } else {
            return new CustomUserDetails(authUser.get());
        }
    }
}
