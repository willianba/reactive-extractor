package com.tcc.extractor.dto;

import java.util.List;

public class TranslationRequest {

  private List<String> repositoriesUrl;
  private List<String> fileExtensions;
  private String sourceLanguage;
  private String targetLanguage;

  public List<String> getRepositoriesUrl() {
    return this.repositoriesUrl;
  }

  public void setRepositoriesUrl(List<String> repositoriesUrl) {
    this.repositoriesUrl = repositoriesUrl;
  }

  public List<String> getFileExtension() {
    return this.fileExtensions;
  }

  public void setFileExtension(List<String> fileExtensions) {
    this.fileExtensions = fileExtensions;
  }

  public String getSourceLanguage() {
    return sourceLanguage;
  }

  public void setSourceLanguage(String sourceLanguage) {
    this.sourceLanguage = sourceLanguage;
  }

  public String getTargetLanguage() {
    return targetLanguage;
  }

  public void setTargetLanguage(String targetLanguage) {
    this.targetLanguage = targetLanguage;
  }
}
