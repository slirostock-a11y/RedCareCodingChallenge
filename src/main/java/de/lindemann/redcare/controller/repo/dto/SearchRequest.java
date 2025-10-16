package de.lindemann.redcare.controller.repo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.lindemann.redcare.client.github.GitHubSortBy;
import de.lindemann.redcare.client.github.GitHubSortOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Request containing all search options")
public class SearchRequest {

    @Schema(description = """
            Filter repositories by programming language.
            You can enter any language recognized by GitHub (e.g., Java, C#, Tree-sitter Query).
            For languages containing spaces or special characters, they will be automatically quoted and URL-encoded.
            If not set, repositories of all languages are returned.
            """, example = "Java")
    private String language;

    @Schema(description = "Maximum number of repositories returned",
            example = "50")
    @Min(value = 0, message = "Limit must be >= 0")
    @Max(value = 1000, message = "Limit must be <= 1000")
    private Integer limit;

    @Schema(description = """
            Minimum score of repositories to include in the results.
            The score is calculated based on repository activity, forks, stars, and recency.
            Only repositories with a score greater than or equal to this value are returned.
            """, example = "30")
    @Min(value = 0, message = "MinimumScore must be >= 0")
    private Integer minimumScore;

    @PastOrPresent
    @Schema(description = "Only include repositories created at this date or later (format: YYYY-MM-DD)", example = "2024-12-31")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdFrom;

    @Schema(description = """
            Sort repositories by a specific field.
            GitHub limits search results to 1000 items, so sorting can help control the results.
            """,
            example = "STARS"
    )
    private GitHubSortBy repoSearchSortBy;

    @Schema(description = "Sort order direction for GitHub-Query.", example = "DESC")
    private GitHubSortOrder repoSearchSortOrder;

    @AssertTrue(message = "createdFrom must be 1990 or later")
    @JsonIgnore
    public boolean isCreatedFromValid() {
        return createdFrom == null || !createdFrom.isBefore(LocalDate.of(1990, 1, 1));
    }

}
