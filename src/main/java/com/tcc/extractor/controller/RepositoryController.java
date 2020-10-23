package com.tcc.extractor.controller;

import com.tcc.extractor.dto.GitHubContentForTranslation;
import com.tcc.extractor.dto.TranslationRequest;
import com.tcc.extractor.rabbitmq.TranslatorPublisher;
import com.tcc.extractor.service.RepositoryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class RepositoryController {

  private static final Logger logger = LoggerFactory.getLogger(RepositoryController.class);

  @Autowired
  private RepositoryService service;

  @Autowired
  private TranslatorPublisher publisher;

  @PostMapping
  public Mono<ResponseEntity<String>> extractFiles(@RequestBody TranslationRequest translationRequest) {
    logger.info("Request received - Started translating files");

    Flux<GitHubContentForTranslation> files = service.extractFiles(translationRequest);
    return files.hasElements()
      .map(hasElement -> {
        if (hasElement) {
          publisher.sendFilesToTranslation(files);
          return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.TEXT_PLAIN)
            .body("Files sent to translation");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .contentType(MediaType.TEXT_PLAIN)
          .body("No files found with the required extension");
      });
  }
}
