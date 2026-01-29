package com.lib.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LibraryApiApplication {

    public static void main(String[] args) {

        SpringApplication.run(LibraryApiApplication.class, args);
    }

}
