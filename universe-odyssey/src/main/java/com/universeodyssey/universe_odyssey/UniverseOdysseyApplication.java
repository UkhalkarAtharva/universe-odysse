package com.universeodyssey.universe_odyssey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class UniverseOdysseyApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniverseOdysseyApplication.class, args);
	}

}
