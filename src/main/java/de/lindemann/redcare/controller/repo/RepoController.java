package de.lindemann.redcare.controller.repo;

import de.lindemann.redcare.service.repo.RepoLanguageService;
import de.lindemann.redcare.service.repo.RepoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RepoController {

    private final RepoService repoService;
    private final RepoLanguageService languageService;

    @GetMapping("/search")
    public RepoResponse hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return repoService.searchAndRateRepos(new RepoRequest());
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
                            description = "GitHub raw URL not found"
                    ),
                    @ApiResponse(
                            responseCode = "424",
                            description = "GitHub raw not accessible (upstream dependency failed)"
                    )
            }
    )
    @GetMapping("/language")
    public RepoLanguageResponse getLanguages() {
        return languageService.getProgrammingLanguages();
    }
}
