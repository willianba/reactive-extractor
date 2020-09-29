package com.tcc.extractor.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.extractor.dto.GitHubContentForTranslation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TranslatorPublisher {

  private static final Logger logger = LoggerFactory.getLogger(TranslatorPublisher.class);

  private ObjectMapper mapper;

  @Autowired
  private AmqpTemplate amqpTemplate;

  @Value("${direct.exchange}")
  private String directExchange;

  @Value("${translate.routing.key}")
  private String translateRoutingKey;

  public TranslatorPublisher() {
    this.mapper = new ObjectMapper();
  }

  public void sendFilesToTranslation(Flux<GitHubContentForTranslation> files) {
    files.flatMap(file -> {
      return Mono.just(
      MessageBuilder.withBody(mapFileToByteArray(file))
        .setContentType(MessageProperties.CONTENT_TYPE_JSON)
        .build());})
    .flatMap(message -> Mono.fromRunnable(() -> amqpTemplate.send(directExchange, translateRoutingKey, message)))
    .doOnComplete(() -> logger.info("Files sent to translation"))
    .subscribe();
  }

  private byte[] mapFileToByteArray(GitHubContentForTranslation file) {
    byte[] result = null;

    try {
      result = mapper.writeValueAsString(file).getBytes();
    } catch (JsonProcessingException e) {
      logger.error("Error converting message to JSON: {}", e.getCause());
    }

    return result;
  }
}
