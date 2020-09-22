package com.tcc.extractor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class ExtractorApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExtractorApplication.class, args);
  }
}
