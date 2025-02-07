package com.kernelcrash.bytebank_server.controllers;

import com.kernelcrash.bytebank_server.models.User;
import com.kernelcrash.bytebank_server.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/allUsers")
    public List<User> getAllUsers() {
        return authService.getAllUsers();
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) throws Exception {
        return authService.login(email, password);
    }

    @PostMapping("/register")
    public boolean register(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        System.out.println("Registering user: " + username);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        return authService.register(username, email, password);
    }

    @PostMapping("/refresh-user")
    public ResponseEntity<User> refreshUser(@RequestParam String email) {
        return authService.refreshUser(email);
    }

    @PostMapping("/logout")
    public boolean logout(@RequestParam String username) {
        return authService.logout(username);
    }

    @PostMapping("/change-password")
    public boolean changePassword(@RequestParam String uuid, @RequestParam String oldPassword, @RequestParam String newPassword) {
        return authService.changePassword(uuid, oldPassword, newPassword);
    }

    @PostMapping("/reset-password")
    public boolean resetPassword(@RequestParam String username) {
        return authService.resetPassword(username);
    }

    @PostMapping("/verify-email")
    public boolean verifyEmail(@RequestParam String username, @RequestParam String email) {
        return authService.verifyEmail(username, email);
    }

    @DeleteMapping("/delete")
    public boolean deleteUser(@RequestParam String email) {
        return authService.deleteUser(email);
    }
}
