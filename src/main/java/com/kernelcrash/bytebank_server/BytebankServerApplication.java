package com.kernelcrash.bytebank_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BytebankServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BytebankServerApplication.class, args);
	}

}
