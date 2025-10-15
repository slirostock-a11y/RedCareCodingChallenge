package de.lindemann.redcare.client.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class GitHubRepositoryDto {

    private Integer id;

    private String fullName;

    private String htmlUrl;

    private String language;

    private String description;

    private Instant createdAt;

    private Instant updatedAt;

    private Integer stargazersCount;

    @JsonProperty("forks")
    private Integer forksCount;

    private boolean allowForking;
}
