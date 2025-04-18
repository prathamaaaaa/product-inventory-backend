package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ProductInventoryBackendApplication {

	public static void main(String[] args) {
		
		Dotenv dotenv = Dotenv.configure()
                .filename("pass.env") 
                .load();

        System.setProperty("db_user", dotenv.get("db_user"));
        System.setProperty("db_url", dotenv.get("db_url"));
        System.setProperty("frontend_url", dotenv.get("frontend_url"));


		SpringApplication.run(ProductInventoryBackendApplication.class, args);
		System.out.println("donesss");
	}

}
