package ru.khokhlov.biletka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableFeignClients
public class BiletkaApplication {
	public static void main(String[] args) {
		SpringApplication.run(BiletkaApplication.class, args);
	}

}
