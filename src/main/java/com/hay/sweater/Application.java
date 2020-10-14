package com.hay.sweater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
    	
//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
//        System.out.println(passwordEncoder.encode("1"));
    	
        SpringApplication.run(Application.class, args);
    }
}
