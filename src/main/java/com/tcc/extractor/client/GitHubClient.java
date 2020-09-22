package com.tcc.extractor.client;

import com.tcc.extractor.dto.GitHubContentForTranslation;
import com.tcc.extractor.dto.GitHubRepositoryContent;
import com.tcc.extractor.helper.ClientHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GitHubClient {

  private static final String GITHUB_JSON = "application/vnd.github.v3+json";
  private static final String GITHUB_RAW = "application/vnd.github.v3.raw";

  private WebClient client;

  @Autowired
  private ClientHelper clientHelper;

  @Value("${github.personal.token}")
  private String gitPersonalToken;

  public GitHubClient() {
    this.client = WebClient.builder()
      .baseUrl("https://api.github.com/repos/")
      .build();
  }

  public Flux<GitHubContentForTranslation> getFilesContent(Flux<GitHubRepositoryContent> files) {
    return files.flatMap(file -> {
        GitHubContentForTranslation result = new GitHubContentForTranslation();
        result.setName(file.getName());
        retrieveFileContent(file)
          .subscribe(content -> result.setContent(content));
        return Mono.just(result);
    });
  }

  private Mono<String> retrieveFileContent(GitHubRepositoryContent file) {
    return client.get()
      .uri(file.getUrl())
      .header(HttpHeaders.ACCEPT, GITHUB_RAW)
      .header(HttpHeaders.AUTHORIZATION, "token " + gitPersonalToken)
      .retrieve()
      .bodyToMono(String.class);
  }

  public Flux<GitHubRepositoryContent> getRepositoryContent(Flux<String> urls) {
    return clientHelper.getDirectoryApiUrl(urls)
      .flatMap(this::retrieveContent);
  }

  private Flux<GitHubRepositoryContent> retrieveContent(String url) {
    return client.get()
      .uri(url)
      .header(HttpHeaders.ACCEPT, GITHUB_JSON)
      .header(HttpHeaders.AUTHORIZATION, "token " + gitPersonalToken)
      .retrieve()
      .bodyToFlux(GitHubRepositoryContent.class);
  }
}
