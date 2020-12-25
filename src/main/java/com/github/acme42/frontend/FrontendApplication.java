package com.github.acme42.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy
public class FrontendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontendApplication.class, args);
	}

}
