package com.ey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ey")
public class LaptopStoreManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaptopStoreManagementSystemApplication.class, args);
	}
}
