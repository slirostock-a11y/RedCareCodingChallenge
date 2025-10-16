package de.lindemann.redcare.client.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRepositoryDto {

    private Long id;

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
