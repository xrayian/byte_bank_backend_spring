package com.kernelcrash.bytebank_server.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kernelcrash.bytebank_server.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionsService transactionsService;

    @Autowired
    public TransactionController(TransactionsService transactionsService) {
        this.transactionsService = transactionsService;
    }

    @GetMapping("/test")
    public String test() {
        return "Transaction Controller is working!";
    }

    @PostMapping("/transfer")
    public boolean transfer(@RequestParam String senderUsername, @RequestParam String receiverUsername, @RequestParam double amount) {
        return transactionsService.transferMoney(senderUsername, receiverUsername, amount);
    }

    @PostMapping("/deposit")
    public boolean deposit(@RequestParam String username, @RequestParam double amount) {
        return transactionsService.depositMoney(username, amount);
    }

    @PostMapping("/withdraw")
    public boolean withdraw(@RequestParam String username, @RequestParam double amount) {
        return transactionsService.withdrawMoney(username, amount);
    }

    private static HashMap<String, String> getUSDExchangeRates() {
        HashMap<String, String> usdRates = new HashMap<>();
        String apiUrl = "https://api.coinbase.com/v2/exchange-rates?currency=USD";
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(apiUrl, String.class);
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
            JsonObject rates = jsonResponse
                    .getAsJsonObject("data")
                    .getAsJsonObject("rates");

            for (String key : rates.keySet()) {
                usdRates.put(key, rates.get(key).getAsString());
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return usdRates;
    }

    @PostMapping("/send-currency-between-users")
    public boolean sendCurrencyBetweenUsers(
            @RequestParam String senderUUID,
            @RequestParam String receiverUUID,
            @RequestParam double amount,
            @RequestParam String currency,
            @RequestParam double senderWalletAddress
    ) {
        //basic validation

        if (senderUUID == null || receiverUUID == null || amount == 0 || currency == null || senderWalletAddress == 0) {
            System.err.println("/send-currency-between-users Invalid parameters");
            System.out.println("senderUUID: " + senderUUID);
            System.out.println("receiverUUID: " + receiverUUID);
            System.out.println("amount: " + amount);
            System.out.println("currency: " + currency);
            System.out.println("senderWalletAddress: " + senderWalletAddress);
            return false;
        }

        if (Objects.equals(currency, "") || Objects.equals(senderUUID, "") || Objects.equals(receiverUUID, "") || Objects.equals(senderWalletAddress, 0) || Objects.equals(amount, 0)) {
            System.err.println("Critical parameters are empty");
            return false;
        }

        HashMap<String, String> usdRates = getUSDExchangeRates();

        return transactionsService.sendCurrencyBetweenUsers(
                senderUUID,
                receiverUUID,
                amount,
                currency,
                senderWalletAddress,
                usdRates
        );
    }

    @PostMapping("/convert-currency-between-wallets")
    public boolean convertBetweenWallets(@RequestParam String uuid, @RequestParam double amount, @RequestParam double fromWalletId, @RequestParam double toWalletId) {

        if (uuid == null || fromWalletId == 0 || toWalletId == 0) {
            System.err.println("/convert-currency-between-wallets Invalid parameters");
            System.out.println("uuid: " + uuid);
            System.out.println("fromWalletId: " + fromWalletId);
            System.out.println("toWalletId: " + toWalletId);
            return false;
        }

        HashMap<String, String> usdRates = getUSDExchangeRates();

        return transactionsService.convertBetweenWallets(uuid, amount, fromWalletId, toWalletId, usdRates);
    }

    @PostMapping("/change-primary-wallet")
    public boolean changePrimaryWallet(@RequestParam String uuid, @RequestParam double newWalletId, @RequestParam double oldWalletId) {
        if (uuid == null || newWalletId == 0 || oldWalletId == 0) {
            System.err.println("/change-primary-wallet Invalid parameters");
            System.out.println("uuid: " + uuid);
            System.out.println("newWalletId: " + newWalletId);
            System.out.println("oldWalletId: " + oldWalletId);
            return false;
        }

        return transactionsService.changePrimaryWallet(uuid, newWalletId, oldWalletId);
    }

    @PostMapping("/open-wallet")
    public ResponseEntity<Boolean> openWallet(@RequestParam String uuid, @RequestParam String walletName, @RequestParam String symbol) {
        if (uuid == null || walletName == null || symbol == null) {

            System.err.println("/open-wallet Invalid parameters");
            System.out.println("uuid: " + uuid);
            System.out.println("walletName: " + walletName);
            System.out.println("symbol: " + symbol);
            return ResponseEntity.badRequest().body(false);
        }

        if (Objects.equals(walletName, "") || Objects.equals(symbol, "")) {
            System.err.println("Wallet name or symbol is empty");
            return ResponseEntity.badRequest().body(false);
        }

        if (transactionsService.openWallet(uuid, walletName, symbol)) {
            return ResponseEntity.ok(true);
        } else {
            System.out.println("Failed to open wallet");
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/buy-crypto")
    public boolean buyCrypto(@RequestParam String username, @RequestParam double amount, @RequestParam String crypto, @RequestParam String walletAddress) {
        return transactionsService.buyCrypto(username, amount, crypto, walletAddress);
    }

    @PostMapping("/sell-crypto")
    public boolean sellCrypto(@RequestParam String username, @RequestParam double amount, @RequestParam String crypto, @RequestParam String walletAddress) {
        return transactionsService.sellCrypto(username, amount, crypto, walletAddress);
    }
}