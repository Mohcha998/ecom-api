package com.ecom.services;

import com.ecom.models.entities.User;
import com.ecom.models.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder; // Menggunakan PasswordEncoder

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Mengkodekan password
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

}