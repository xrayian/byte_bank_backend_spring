package com.kernelcrash.bytebank_server.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "wallets")
public class Wallet {
    public Wallet(Long walletId, String walletName, String cryptoType, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isPrimary, List<Transaction> transactions, User user) {
        this.walletId = walletId;
        this.walletName = walletName;
        this.cryptoType = cryptoType;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isPrimary = isPrimary;
        this.transactions = transactions;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long walletId;               // Unique identifier for the wallet
    private String walletName;           // Custom name for the wallet
    private String cryptoType;           // Type of cryptocurrency (e.g., BTC, ETH)

    private LocalDateTime createdAt;            // Creation timestamp
    private LocalDateTime updatedAt;            // Last update timestamp
    private boolean isPrimary;   // Primary wallet flag

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions; // List of wallet transactions

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;                   // Associated user

    public Wallet() {

    }

    public Wallet(String walletName, String cryptoType, LocalDateTime createdAt, LocalDateTime updatedAt, List<Transaction> transactions, User user) {
        this.walletName = walletName;
        this.cryptoType = cryptoType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.transactions = transactions;
        this.user = user;
    }

    // Getters and Setters

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getCryptoType() {
        return cryptoType;
    }

    public void setCryptoType(String cryptoType) {
        this.cryptoType = cryptoType;
    }

    public double getBalance() {
        return getTransactions().stream().mapToDouble(Transaction::getAmount).sum();
    }

    public boolean isPrimary() {
        return Objects.equals(user.getWallets().get(0).getWalletId(), walletId);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = LocalDateTime.parse(updatedAt);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void setCreatedAt(LocalDateTime now) {
        this.createdAt = now;
    }

    public void setUpdatedAt(LocalDateTime now) {
        this.updatedAt = now;
    }

//    public void setIsPrimary(boolean b) {
//        this.isPrimary = b;
//    }

    public void setUser(User newUser) {
        this.user = newUser;
    }

    public void setIsPrimary(boolean b) {
        this.isPrimary = b;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}