package com.ajou_nice.with_pet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class WithPetApplication {

    public static void main(String[] args) {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
        SpringApplication.run(WithPetApplication.class, args);
    }

}
