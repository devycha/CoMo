package com.dongjji.como;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CoMoApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoMoApplication.class, args);
    }
}
