package com.tcc.extractor.service;

import java.util.List;

import com.tcc.extractor.client.GitHubClient;
import com.tcc.extractor.dto.GitHubContentForTranslation;
import com.tcc.extractor.dto.GitHubRepositoryContent;
import com.tcc.extractor.dto.TranslationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service
public class RepositoryService {

  private static final String CONTENT_TYPE = "file";

  @Autowired
  private GitHubClient client;

  public Flux<GitHubContentForTranslation> extractFiles(TranslationRequest translationRequest) {
    Flux<GitHubRepositoryContent> repositoryContent = client.getRepositoryContent(Flux.fromIterable(translationRequest.getRepositoriesUrl()));
    Flux<GitHubRepositoryContent> filteredContent = filterRepositoryContentByType(repositoryContent, translationRequest.getFileExtension());
    Flux<GitHubContentForTranslation> filesContent = client.getFilesContent(filteredContent);

    filesContent.subscribe(file -> {
      file.setSourceLanguage(translationRequest.getSourceLanguage());
      file.setTargetLanguage(translationRequest.getTargetLanguage());
    });

    return filesContent;
  }

  private Flux<GitHubRepositoryContent> filterRepositoryContentByType(Flux<GitHubRepositoryContent> repositoryContent, List<String> extensions) {
    return repositoryContent
      .filter(contentFile -> contentFile.getType().equals(CONTENT_TYPE))
      .filter(contentFile -> extensions.stream().anyMatch(ext -> contentFile.getName().contains(ext)));
  }
}
