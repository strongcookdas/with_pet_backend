package com.ajou_nice.with_pet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing
public class WithPetApplication {

    public static void main(String[] args) {
        SpringApplication.run(WithPetApplication.class, args);
    }

}
