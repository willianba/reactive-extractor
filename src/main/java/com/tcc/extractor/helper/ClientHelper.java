package com.tcc.extractor.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ClientHelper {

  public Flux<String> getDirectoryApiUrl(Flux<String> urls) {
    return urls
      .flatMap(this::getUserAndRepo)
      .flatMap(this::getDirectoryFormattedApiUrl);
  }

  private Mono<String[]> getUserAndRepo(String gitUrl) {
    Pattern pattern = Pattern.compile("\\.com/(.*)");
    Matcher matcher = pattern.matcher(gitUrl);
    matcher.find(); // TODO remove, bad practice
    return Mono.just(matcher.group(1).split("/"));
  }

  private Mono<String> getDirectoryFormattedApiUrl(String[] userAndRepo) {
    StringBuilder sb = new StringBuilder();
    String user = userAndRepo[0];
    String repo = userAndRepo[1];
    String formattedUrl = sb.append(user)
      .append("/")
      .append(repo)
      .append("/contents")
      .toString();
    return Mono.just(formattedUrl);
  }
}
