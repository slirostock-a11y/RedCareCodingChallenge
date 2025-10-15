package de.lindemann.redcare.client.github;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GitHubApiClient {
    private final WebClient webClient;

    public GitHubApiClient(WebClient.Builder builder,
                           @Value("${githubclient.api-url}") String apiUrl,
                           @Value("${githubclient.token}") String token) {

        this.webClient = builder
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    public GitHubSearchDto searchRepos(
            @NotBlank String query,
            @NotNull @Min(1) @Max(10) Integer page) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/repositories")
                        .queryParam("q", query)
                        .queryParam("per_page", 100)
                        .queryParam("page", page)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("Repository not found!")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("GitHub server error")))
                .bodyToMono(GitHubSearchDto.class)
                .block();
    }
}
