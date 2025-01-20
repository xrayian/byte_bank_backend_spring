package com.kernelcrash.bytebank_server.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    @MessageMapping("/wallet")
    @SendTo("/topic/wallet-updates")
    public String handleWalletUpdates(String message) {
        // Process the message and return a response
        return "Wallet update received: " + message;
    }
}
