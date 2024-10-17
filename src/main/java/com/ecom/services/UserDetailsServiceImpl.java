package com.ecom.services;

import com.ecom.models.entities.User;
import com.ecom.models.entities.Role;
import com.ecom.models.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Gunakan Optional.ofNullable agar tidak error jika null
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByUsername(username));

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = optionalUser.get();
        
        // Gunakan UserBuilder untuk membangun objek UserDetails dari user yang ditemukan
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
        builder.password(user.getPassword());
        builder.roles(user.getRoles().stream().map(Role::getName).toArray(String[]::new));

        return builder.build();
    }
}
