package com.foodcourt.foodcourt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FoodCourtApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(
			FoodCourtApplication.class,
			args
		);
	}
	
}
