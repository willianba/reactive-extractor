package com.tcc.extractor.dto;

public class GitHubRepositoryContent {

  private String name;
  private String url;
  private String downloadUrl;
  private String type;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDownloadUrl() {
    return this.downloadUrl;
  }

  public void setDownloadUrl(String downloadUrl) {
    this.downloadUrl = downloadUrl;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
