package com.parimal.e_wholesaler.shop_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(
		exclude = { UserDetailsServiceAutoConfiguration.class },
		scanBasePackages = {
				"com.parimal.e_wholesaler.common",
				"com.parimal.e_wholesaler.shop_service"
		}
)
@EnableDiscoveryClient
@EnableFeignClients
public class ShopServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopServiceApplication.class, args);
	}

}
