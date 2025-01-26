package com.kernelcrash.bytebank_server.services;


import com.kernelcrash.bytebank_server.models.Transaction;
import com.kernelcrash.bytebank_server.models.User;
import com.kernelcrash.bytebank_server.models.Wallet;
import com.kernelcrash.bytebank_server.repositories.TransactionRepository;
import com.kernelcrash.bytebank_server.repositories.UserRepository;
import com.kernelcrash.bytebank_server.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionsService {
    UserRepository userRepository;
    WalletRepository walletRepository;
    TransactionRepository transactionRepository;

    @Autowired
    public TransactionsService(UserRepository userRepository, WalletRepository walletRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
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

    public boolean convertBetweenWallets(String uuid, double amount, double fromWalletId, double toWalletId, HashMap<String, String> usdRates) {
        // Validate user
        Optional<User> user = userRepository.findById(uuid);
        if (user.isEmpty()) return false;

        // Validate amount and wallet IDs
        if (amount <= 0 || Objects.equals(fromWalletId, toWalletId)) return false;

        // Fetch wallets
        Optional<Wallet> fromWalletOpt = walletRepository.findById((long) fromWalletId);
        Optional<Wallet> toWalletOpt = walletRepository.findById((long) toWalletId);
        if (fromWalletOpt.isEmpty() || toWalletOpt.isEmpty()) return false;

        Wallet fromWallet = fromWalletOpt.get();
        Wallet toWallet = toWalletOpt.get();

        // Validate wallet ownership and balance
        if (!user.get().getWallets().contains(fromWallet) || !user.get().getWallets().contains(toWallet)) return false;
        if (fromWallet.getBalance() < amount) return false;

        // Validate exchange rates
        if (!usdRates.containsKey(fromWallet.getCryptoType()) || !usdRates.containsKey(toWallet.getCryptoType())) {
            throw new IllegalArgumentException("Missing exchange rate for one or both wallet types.");
        }

        // Perform conversion using BigDecimal
        BigDecimal fromRate = new BigDecimal(1).divide(new BigDecimal(usdRates.get(fromWallet.getCryptoType())), 10, RoundingMode.HALF_UP);
        BigDecimal toRate = new BigDecimal(1).divide(new BigDecimal(usdRates.get(toWallet.getCryptoType())), 10, RoundingMode.HALF_UP);

        // Perform the conversion
        BigDecimal convertedAmount = BigDecimal.valueOf(amount).multiply(fromRate).divide(toRate, RoundingMode.HALF_UP);

        fromWallet.setUpdatedAt(LocalDateTime.now());
        toWallet.setUpdatedAt(LocalDateTime.now());


        Transaction fromTrx = createTransaction(fromWallet, -amount,
                "Converted " + amount + " from " + fromWallet.getCryptoType() + " to " + toWallet.getCryptoType(), "Conversion");
        Transaction toTrx = createTransaction(toWallet, convertedAmount.doubleValue(),
                "Received " + convertedAmount + " in " + toWallet.getCryptoType() + " from conversion.", "Conversion");

        fromWallet.getTransactions().add(fromTrx);
        toWallet.getTransactions().add(toTrx);

        walletRepository.save(fromWallet);
        walletRepository.save(toWallet);

        return true;
    }

    private Transaction createTransaction(Wallet wallet, double amount, String description, String type) {
        Transaction trx = new Transaction();
        trx.setAmount(amount);
        trx.setTimestamp(String.valueOf(System.currentTimeMillis()));
        trx.setWallet(wallet);
        trx.setType(type);
        trx.setDescription(description);
        return trx;
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

    public boolean sendCurrencyBetweenUsers(String senderUUID, String receiverUUID, double amount, String currency, double senderWalletId, HashMap<String, String> usdRates) {
        // Validate sender and receiver
        Optional<User> sender = userRepository.findById(senderUUID);
        Optional<User> receiver = userRepository.findById(receiverUUID);
        if (sender.isEmpty() || receiver.isEmpty()) return false;

        // Validate sender wallet
        Optional<Wallet> senderWalletOpt = walletRepository.findById((long) senderWalletId);
        if (senderWalletOpt.isEmpty()) return false;

        Wallet senderWallet = senderWalletOpt.get();
        if (!sender.get().getWallets().contains(senderWallet)) return false;

        // Validate balance
        if (amount <= 0 || senderWallet.getBalance() < amount) return false;

        // Validate currency and exchange rates
        if (!usdRates.containsKey(currency)) {
            throw new IllegalArgumentException("Exchange rate for the provided currency is missing.");
        }

        // Get receiver's primary wallet
        Wallet receiverPrimaryWallet = receiver.get().getWallets().stream()
                .filter(Wallet::isPrimary) // Assuming the `Wallet` entity has an `isPrimary` field
                .findFirst()
                .orElse(null);

        if (receiverPrimaryWallet == null) {
            throw new IllegalArgumentException("Receiver does not have a primary wallet.");
        }

        if (!usdRates.containsKey(receiverPrimaryWallet.getCryptoType())) {
            throw new IllegalArgumentException("Exchange rate for receiver's primary wallet type is missing.");
        }

        //todo migrate to https://min-api.cryptocompare.com/data/pricemulti?fsyms=usd,...&tsyms=btc,... for better rates

        BigDecimal senderCurrencyRate = new BigDecimal(1).divide(new BigDecimal(usdRates.get(currency)), 10, RoundingMode.HALF_UP);
        BigDecimal receiverCurrencyRate = new BigDecimal(1).divide(new BigDecimal(usdRates.get(receiverPrimaryWallet.getCryptoType())), 10, RoundingMode.HALF_UP);

        BigDecimal convertedAmount = BigDecimal.valueOf(amount).multiply(senderCurrencyRate).divide(receiverCurrencyRate, RoundingMode.HALF_UP);

        senderWallet.setUpdatedAt(LocalDateTime.now());
        receiverPrimaryWallet.setUpdatedAt(LocalDateTime.now());

        // Create transactions
        Transaction senderTrx = createTransaction(senderWallet, -amount,
                "Sent " + amount + " " + currency + " to " + receiverUUID + ", converted to " + convertedAmount + " " + receiverPrimaryWallet.getCryptoType(), "Transfer");
        Transaction receiverTrx = createTransaction(receiverPrimaryWallet, convertedAmount.doubleValue(),
                "Received " + convertedAmount + " " + receiverPrimaryWallet.getCryptoType() + " from " + senderUUID + ", sent in " + amount + " " + currency, "Transfer");

        senderWallet.getTransactions().add(senderTrx);
        receiverPrimaryWallet.getTransactions().add(receiverTrx);

        // Save updates
        walletRepository.save(senderWallet);
        walletRepository.save(receiverPrimaryWallet);

        return true;
    }

    public boolean changePrimaryWallet(String uuid, double newWalletId, double oldWalletId) {
        Optional<User> user = userRepository.findById(uuid);
        if (user.isEmpty()) return false;

        Optional<Wallet> newWalletOpt = walletRepository.findById((long) newWalletId);
        Optional<Wallet> oldWalletOpt = walletRepository.findById((long) oldWalletId);
        if (newWalletOpt.isEmpty() || oldWalletOpt.isEmpty()) return false;

        Wallet newWallet = newWalletOpt.get();
        Wallet oldWallet = oldWalletOpt.get();

        if (!user.get().getWallets().contains(newWallet) || !user.get().getWallets().contains(oldWallet)) return false;

        user.get().getWallets().forEach(wallet -> {
            if (wallet.getWalletId().equals(oldWallet.getWalletId())) {
                wallet.setIsPrimary(false);
            } else if (wallet.getWalletId().equals(newWallet.getWalletId())) {
                wallet.setIsPrimary(true);
            }
        });

        userRepository.save(user.get());
        return true;
    }
}
