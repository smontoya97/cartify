package com.smontoya.cartify;

import org.springframework.boot.SpringApplication;

public class TestCartifyApplication {

	public static void main(String[] args) {
		SpringApplication.from(CartifyApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
