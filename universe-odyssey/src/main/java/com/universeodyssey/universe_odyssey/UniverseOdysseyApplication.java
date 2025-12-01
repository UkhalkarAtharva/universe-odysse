package com.universeodyssey.universe_odyssey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@org.springframework.context.annotation.ComponentScan(basePackages = "com.universeodyssey")
@org.springframework.data.jpa.repository.config.EnableJpaRepositories(basePackages = "com.universeodyssey")
@org.springframework.boot.autoconfigure.domain.EntityScan(basePackages = "com.universeodyssey")
public class UniverseOdysseyApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniverseOdysseyApplication.class, args);
	}

}
