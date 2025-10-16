package de.lindemann.redcare.controller.repo;

import de.lindemann.redcare.adapter.RepoAdapter;
import de.lindemann.redcare.config.exception.ErrorResponse;
import de.lindemann.redcare.controller.repo.dto.RepoLanguageResponse;
import de.lindemann.redcare.controller.repo.dto.SearchRequest;
import de.lindemann.redcare.controller.repo.dto.SearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repository")
@Tag(name = "GitHub Repository Search", description = "Search GitHub repositories with advanced filters and computed score")
public class RepoController {

    private final RepoAdapter repoAdapter;

    @Operation(
            summary = "Search repositories on GitHub",
            description = """
                    Retrieves repositories from GitHub matching the provided filters. 
                    GitHub limits search results to 1000 items. 
                    The service fetches all pages from GitHub, computes a score for each repository, 
                    sorts by score descending, and applies any optional minimumScore or limit filters.
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of repositories matching the filters",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SearchResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "GitHub API or requested resource not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "424",
                            description = "GitHub API not accessible; dependency failure",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/search")
    public SearchResponse search(@ModelAttribute SearchRequest request,
                                 @Parameter(name = "X-GitHub-Token",
                                         description = """
                                                 Optional GitHub Personal Access Token to bypass default rate limits.
                                                 If provided, the backend will use this token for GitHub API calls instead of the server-wide default token.
                                                 Be aware: the token is sent over HTTPS and will be used only for this request.
                                                 """,
                                         in = ParameterIn.HEADER
                                 )
                                 @RequestHeader(value = "X-GitHub-Token", required = false) String personalToken) {
        return repoAdapter.search(request, personalToken);
    }

    @Operation(
            summary = "Retrieve all programming languages from GitHub Linguist",
            description = "Returns an alphabetically sorted list of all programming languages classified as `programming` by GitHub Linguist.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully returned list of languages",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RepoLanguageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "GitHub raw URL not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "424",
                            description = "GitHub raw not accessible (upstream dependency failed)",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/languages")
    public RepoLanguageResponse getLanguages() {
        return repoAdapter.getLanguages();
    }
}
