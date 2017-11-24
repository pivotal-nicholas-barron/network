package com.social.network;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class NetworkApplication {
    public static void main(String[] args) {
        SpringApplication.run(NetworkApplication.class, args);
    }
}
