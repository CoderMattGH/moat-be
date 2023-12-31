package com.moat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class MoatApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoatApplication.class, args);
    }
}