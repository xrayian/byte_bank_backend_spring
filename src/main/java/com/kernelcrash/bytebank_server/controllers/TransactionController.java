package com.kernelcrash.bytebank_server.controllers;

import com.kernelcrash.bytebank_server.services.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/exchange")
    public boolean exchange(@RequestParam String username, @RequestParam double amount, @RequestParam String currency, @RequestParam String walletAddress) {
        return transactionsService.exchangeCurrency(username, amount, currency, walletAddress);
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