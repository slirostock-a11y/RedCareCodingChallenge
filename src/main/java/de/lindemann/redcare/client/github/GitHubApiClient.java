package de.lindemann.redcare.client.github;

import de.lindemann.redcare.client.github.dto.GitHubSearchDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.Optional;

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
            String query,
            Integer page,
            Integer pageSize,
            GitHubSortBy sortBy,
            GitHubSortOrder sortOrder,
            String personalToken) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/repositories")
                        .queryParam("q", query)
                        .queryParam("per_page", pageSize)
                        .queryParam("page", page)
                        .queryParamIfPresent("sort", Optional.ofNullable(sortBy).map(GitHubSortBy::getValue))
                        .queryParamIfPresent("order", Optional.ofNullable(sortOrder).map(GitHubSortOrder::getValue))
                        .build())
                .headers(headers -> {
                    if (StringUtils.isNotBlank(personalToken)) {
                        // Override Authorization only for this request
                        headers.setBearerAuth(personalToken);
                    }
                })
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::handle4xx)
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new ResponseStatusException(
                                HttpStatus.FAILED_DEPENDENCY, "GitHub API not accessible")))
                .bodyToMono(GitHubSearchDto.class)
                .block();
    }

    private Mono<? extends Throwable> handle4xx(ClientResponse response) {
        return Mono.defer(() -> {
            int status = response.statusCode().value();
            return switch (status) {
                case 401 -> Mono.error(new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "GitHub token not accepted"));
                case 403 -> Mono.error(new ResponseStatusException(
                        HttpStatus.FORBIDDEN, "GitHub API rate limit exceeded"));
                case 404 -> Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Repository not found!"));
                default -> Mono.error(new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Client error with GitHub API"));
            };
        });
    }
}
