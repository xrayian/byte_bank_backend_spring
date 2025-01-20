package com.kernelcrash.bytebank_server.controllers;

import com.google.gson.Gson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/crypto-data")
public class CryptoDataController {

    @GetMapping("/test")
    public String test() {
        return "API Wrapper is working!";
    }


    @GetMapping("/getKlines")
    public ResponseEntity<String> getKlines(@RequestParam String fsymbol, @RequestParam String tosymbol, @RequestParam int aggregate, @RequestParam int limit) {
        System.out.println("Fetching data from CryptoCompare...");
        String apiUrl = "https://min-api.cryptocompare.com/data/v2/histoday?fsym=" + fsymbol + "&tsym=" + tosymbol + "&limit=" + limit + "&aggregate=" + aggregate;
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(apiUrl, String.class);
            System.out.println();
            System.out.println(response);
            System.out.println();
            Gson gson = new Gson();
            ApiResponse responseObj = gson.fromJson(response, ApiResponse.class);
            return ResponseEntity.ok(gson.toJson(responseObj.Data.Data));
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }

    @GetMapping("/multiSymbolPrice")
    public ResponseEntity<Object> multiSymbolPrice(@RequestParam List<String> fromSymbols, @RequestParam List<String> toSymbols) {
        System.out.println("Fetching data from CryptoCompare...");
        String apiUrl = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=" + String.join(",", fromSymbols) + "&tsyms=" + String.join(",", toSymbols);
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(apiUrl, String.class);
            Gson gson = new Gson();
            String responseObj = gson.fromJson(response, String.class);
            return ResponseEntity.ok(responseObj);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    public class ApiResponse {
        private String Response;
        private String Message;
        private boolean HasWarning;
        private int Type;
        private RateLimit RateLimit;
        private Data Data;

    }

    public class Data {
        private boolean Aggregated;
        private long TimeFrom;
        private long TimeTo;
        private List<ChartData> Data;
    }

    public class ChartData {
        private long time;
        private BigDecimal high;
        private BigDecimal low;
        private BigDecimal open;
        private BigDecimal volumefrom;
        private BigDecimal volumeto;
        private BigDecimal close;
        private String conversionType;
        private String conversionSymbol;
    }

    public class RateLimit {
    }

}

