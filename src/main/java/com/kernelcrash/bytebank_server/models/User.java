package com.kernelcrash.bytebank_server.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;
    private String username;

    @Column(unique = true)
    private String email;

    private String passwordHash;
    private String role;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    private Long primaryWalletId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Wallet> wallets;

    public User() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets;
    }

    public void addWallet(Wallet wallet) {
        this.wallets.add(wallet);
    }

    public void removeWallet(Wallet wallet) {
        this.wallets.remove(wallet);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = LocalDateTime.parse(createdAt);
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = LocalDateTime.parse(updatedAt);
    }

    public User(
            String userId, String username, String email, String passwordHash, double accountBalance, List<Wallet> wallets, String role, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, Long primaryWalletId) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.wallets = wallets;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
//        this.primaryWalletId = primaryWalletId;
    }

    public User(
            String username, String email, String passwordHash, double accountBalance, List<Wallet> wallets, String role, boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, Long primaryWalletId) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.wallets = wallets;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
//        this.primaryWalletId = primaryWalletId;
    }

//    public Long getPrimaryWalletId() {
//        return primaryWalletId;
//    }

//    public void setPrimaryWalletId(Long primaryWalletId) {
//        this.primaryWalletId = primaryWalletId;
//    }
}
