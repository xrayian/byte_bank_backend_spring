package com.kernelcrash.bytebank_server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class CryptoSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RestTemplate restTemplate;

    @Autowired
    public CryptoSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
        this.restTemplate = new RestTemplate();
    }

    @Scheduled(fixedRate = 5000) // Fetch data every 5 seconds
    public void fetchCryptoPrices() {
        String apiUrl = "https://api.coinbase.com/v2/exchange-rates?currency=USD";

        try {
            String response = restTemplate.getForObject(apiUrl, String.class);
            messagingTemplate.convertAndSend("/topic/crypto-prices", response);
        } catch (Exception e) {
            System.err.println("Error fetching crypto prices: " + e.getMessage());
        }
    }
}
