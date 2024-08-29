package com.scm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.scm"})
public class Scm2Application {

	public static void main(String[] args) {
		SpringApplication.run(Scm2Application.class, args);
	}

}
