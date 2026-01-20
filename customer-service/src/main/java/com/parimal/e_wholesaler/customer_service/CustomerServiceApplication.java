package com.parimal.e_wholesaler.customer_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
		exclude = { UserDetailsServiceAutoConfiguration.class },
		scanBasePackages = {
				"com.parimal.e_wholesaler.common",
				"com.parimal.e_wholesaler.customer_service"
		}
)
@EnableDiscoveryClient
@EnableFeignClients
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

}
