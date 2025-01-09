package com.kernelcrash.bytebank_server.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long walletId;               // Unique identifier for the wallet
    private String walletName;           // Custom name for the wallet
    private String cryptoType;           // Type of cryptocurrency (e.g., BTC, ETH)
    private double balance;              // Current wallet balance
    private String createdAt;            // Creation timestamp
    private String updatedAt;            // Last update timestamp

    @Transient
    private boolean isPrimary;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions; // List of wallet transactions

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;                   // Associated user

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
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isPrimary() {
        return Objects.equals(user.getPrimaryWalletId(), walletId);
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}