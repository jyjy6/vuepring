package com.spvue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpvueApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpvueApplication.class, args);
		System.out.println("안녕하세요");
		
	}

}
