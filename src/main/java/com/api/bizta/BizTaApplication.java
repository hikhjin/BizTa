package com.api.bizta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BizTaApplication {
    public static void main(String[] args) {
        SpringApplication.run(BizTaApplication.class, args);
    }

}
