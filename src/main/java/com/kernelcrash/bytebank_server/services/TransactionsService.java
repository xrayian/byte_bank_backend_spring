package com.kernelcrash.bytebank_server.services;


import com.kernelcrash.bytebank_server.models.User;
import com.kernelcrash.bytebank_server.models.Wallet;
import com.kernelcrash.bytebank_server.repositories.UserRepository;
import com.kernelcrash.bytebank_server.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionsService {
    UserRepository userRepository;
    WalletRepository walletRepository;

    @Autowired
    public TransactionsService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public boolean transferMoney(String senderUsername, String receiverUsername, double amount) {

        return false;

    }

    public boolean depositMoney(String username, double amount) {

        return false;

    }

    public boolean withdrawMoney(String username, double amount) {

        return false;

    }

    public boolean exchangeCurrency(String username, double amount, String currency, String walletAddress) {

        return false;

    }


    public boolean openWallet(String uuid, String walletName, String currency) {
        Optional<User> user = userRepository.findById(uuid);
        if (user.isEmpty()) {
            return false;
        }
        Wallet wallet = new Wallet();
        wallet.setWalletName(walletName);
        wallet.setCryptoType(currency);
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setUpdatedAt(LocalDateTime.now());
        wallet.setUser(user.get());
        user.get().addWallet(wallet);
        userRepository.save(user.get());
        return true;
    }

    public boolean buyCrypto(String username, double amount, String crypto, String walletAddress) {

        return false;

    }

    public boolean sellCrypto(String username, double amount, String crypto, String walletAddress) {

        return false;

    }
}
