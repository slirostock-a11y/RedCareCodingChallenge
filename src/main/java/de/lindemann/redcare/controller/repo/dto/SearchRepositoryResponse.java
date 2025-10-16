package de.lindemann.redcare.controller.repo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
@Schema(description = "Search response for a repository, including computed score based on activity, forks, stars, and recency")
public class SearchRepositoryResponse {

    @Schema(description = "GitHub repository ID", example = "221862")
    private Long gitHubId;

    @Schema(description = "Full name of the repository (owner/name)", example = "projectlombok/lombok")
    private String fullName;

    @Schema(description = "URL to the repository on GitHub", example = "https://github.com/projectlombok/lombok")
    private String htmlUrl;

    @Schema(description = "Primary programming language of the repository", example = "Java")
    private String language;

    @Schema(description = "Description of the repository", example = "Example repository for demonstration purposes")
    private String description;

    @Schema(description = "Repository creation timestamp", example = "2009-06-08T19:46:41Z")
    private Instant createdAt;

    @Schema(description = "Repository last updated timestamp", example = "2025-10-14T03:06:22Z")
    private Instant updatedAt;

    @Schema(description = "Number of stars of the repository", example = "13293")
    private Integer stargazersCount;

    @Schema(description = "Number of forks of the repository", example = "2470")
    private Integer forksCount;

    @Schema(description = "Whether the repository allows forking", example = "true")
    private boolean allowForking;

    @Schema(description = "Computed score based on activity, forks, stars, and recency", example = "294")
    private Integer score;
}
