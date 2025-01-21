package com.kernelcrash.bytebank_server.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kernelcrash.bytebank_server.models.Wallet;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.ZoneId;


@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;         // Unique identifier for the transaction
    private String type;                // Type of transaction (e.g., DEPOSIT, WITHDRAWAL)
    private double amount;              // Transaction amount
    private String description;         // Optional transaction description
    private String timestamp;           // Timestamp of the transaction

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;              // Associated wallet

    public Transaction() {
    }


    // Getters and Setters

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
