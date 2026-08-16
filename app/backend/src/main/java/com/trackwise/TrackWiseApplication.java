package com.trackwise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TrackWiseApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackWiseApplication.class, args);
    }
}
