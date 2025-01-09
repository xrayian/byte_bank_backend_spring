package com.kernelcrash.bytebank_server.services;

import com.kernelcrash.bytebank_server.models.User;
import com.kernelcrash.bytebank_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String username, String password) {
        //proper implementation
        return true;
    }

    public boolean register(
        String username,
        String email,
        String password
    ) {
        //proper implementation

        return true;
    }

    public boolean logout(String username) {
        return true;
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return true;
    }

    public boolean resetPassword(String username) {
        return true;
    }

    public boolean verifyEmail(String username, String email) {
        return true;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
