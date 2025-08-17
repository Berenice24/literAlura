// GutendexBookDto.java
package com.bere.literalura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GutendexBookDto {

    private String title;

    private List<String> languages;

    private List<GutendexAuthorDto> authors;

    @JsonAlias("download_count")
    private int downloadCount;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<String> getLanguages() { return languages; }
    public void setLanguages(List<String> languages) { this.languages = languages; }

    public List<GutendexAuthorDto> getAuthors() { return authors; }
    public void setAuthors(List<GutendexAuthorDto> authors) { this.authors = authors; }

    public int getDownloadCount() { return downloadCount; }
    public void setDownloadCount(int downloadCount) { this.downloadCount = downloadCount; }
}
