package com.tcc.extractor.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ClientHelper {

  private static final String FILE = "blob";
  private static final String DIRECTORY = "tree";

  public Flux<String> getDirectoryApiUrl(Flux<String> urls) {
    return urls
      .flatMap(this::getUserAndRepo)
      .flatMap(this::getDirectoryFormattedApiUrl);
  }

  private Mono<String> getUserAndRepo(String gitUrl) {
    Pattern pattern = Pattern.compile("\\.com/(.*)");
    Matcher matcher = pattern.matcher(gitUrl);
    matcher.find(); // TODO remove, bad practice
    return Mono.just(matcher.group(1));
  }

  private Mono<String> getDirectoryFormattedApiUrl(String repoPath) {
    StringBuilder sb = new StringBuilder();
    String[] urlParameters = getUrlParameters(repoPath);
    String formattedUrl = sb.append(urlParameters[0])
      .append("/")
      .append(urlParameters[1])
      .append("/contents/")
      .append(urlParameters[2])
      .toString();
    return Mono.just(formattedUrl);
  }

  private String[] getUrlParameters(String repoPath) {
    String[] splittedPath = repoPath.split("/");
    if (repoPath.contains(FILE)) {
      return new String[] {
        splittedPath[0],
        splittedPath[1],
        splittedPath[4] + "/" + splittedPath[5] // ugly af but no time brother, also tighy coupled to my repo
      };
    }

    if (repoPath.contains(DIRECTORY)) {
      return new String[] {
        splittedPath[0],
        splittedPath[1],
        splittedPath[4]
      };
    }

    return new String[] { // array for a root's repo url
      splittedPath[0],
      splittedPath[1],
      ""
    };
  }
}
