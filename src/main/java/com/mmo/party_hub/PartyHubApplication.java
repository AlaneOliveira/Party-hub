package com.mmo.party_hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PartyHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(PartyHubApplication.class, args);
	}

}
