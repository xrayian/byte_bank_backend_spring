package com.kernelcrash.bytebank_server.services;

import com.kernelcrash.bytebank_server.models.Transaction;
import com.kernelcrash.bytebank_server.models.User;
import com.kernelcrash.bytebank_server.models.Wallet;
import com.kernelcrash.bytebank_server.repositories.UserRepository;
import com.kernelcrash.bytebank_server.repositories.WalletRepository;
import com.kernelcrash.bytebank_server.utils.HashingUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService {

    UserRepository userRepository;
    WalletRepository walletRepository;

    @Autowired
    public AuthService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public ResponseEntity<User> login(String email, String password) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            //send response to client
            return ResponseEntity.badRequest().body(null);
        }

        String username = user.getUsername();

        if (!user.getPasswordHash().equals(HashingUtil.hashWithSHA256(username + password))) {
            //send response to client
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(user);
    }

    public boolean register(
            String username,
            String email,
            String password
    ) {
        // Check if the username is already taken
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return false;
        }

        // Initial User and Wallet properties
        double initialAccountBalance = 0.0;
        String role = "Standard User";
        boolean isActive = true;
        LocalDateTime now = LocalDateTime.now();

        // Create a new user object
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPasswordHash(HashingUtil.hashWithSHA256(username + password)); // Hashed password
        newUser.setRole(role);
        newUser.setActive(isActive);
        newUser.setCreatedAt(now);
        newUser.setUpdatedAt(now);

        // Create a primary wallet and associate it with the user
        Wallet primaryWallet = new Wallet();
        primaryWallet.setWalletName("Primary Wallet");
        primaryWallet.setCryptoType("USD");
        primaryWallet.setCreatedAt(now);
        primaryWallet.setUpdatedAt(now);
        primaryWallet.setIsPrimary(true);
        primaryWallet.setUser(newUser); // Set the relationship
        primaryWallet.setTransactions(new ArrayList<>()); // Initialize transactions

        // Set wallets to the user
        List<Wallet> wallets = new ArrayList<>();
        wallets.add(primaryWallet);
        newUser.setWallets(wallets);

        // Save the user (this will cascade and save the wallet due to relationships)

        //add a 500 USD transaction to the primary wallet
        Transaction transaction = new Transaction();
        transaction.setAmount(500.0);
        transaction.setType("Welcome Bonus");
        transaction.setDescription("Welcome Bonus");
        transaction.setWallet(primaryWallet);
        transaction.setTimestamp(String.valueOf(System.currentTimeMillis()));
        List<Transaction> transactions = primaryWallet.getTransactions();
        transactions.add(transaction);
        primaryWallet.setTransactions(transactions);

        userRepository.save(newUser);
        return true;
    }

    public boolean deleteUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }


    public boolean logout(String username) {
        return true;
    }

    @Transactional
    public boolean changePassword(String username, String oldPassword, String newPassword) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        if (!user.getPasswordHash().equals(HashingUtil.hashWithSHA256(username + oldPassword))) {
            return false;
        }

        user.setPasswordHash(HashingUtil.hashWithSHA256(username + newPassword));

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

    public ResponseEntity<User> refreshUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}
