package de.lindemann.redcare.service.repo;

import de.lindemann.redcare.client.github.GitHubApiClient;
import de.lindemann.redcare.client.github.GitHubSortBy;
import de.lindemann.redcare.client.github.GitHubSortOrder;
import de.lindemann.redcare.client.github.dto.GitHubRepositoryDto;
import de.lindemann.redcare.client.github.dto.GitHubSearchDto;
import de.lindemann.redcare.controller.repo.dto.SearchRepositoryResponse;
import de.lindemann.redcare.controller.repo.dto.SearchRequest;
import de.lindemann.redcare.controller.repo.dto.SearchResponse;
import de.lindemann.redcare.mapper.RepoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepoSearchService {

    private final GitHubApiClient gitHubClient;
    private final RepoScoreService scoreService;
    private final RepoQueryService queryService;
    private final RepoMapper repositoryMapper;

    public SearchResponse searchAndRateRepos(SearchRequest repoRequest, String personalToken) {
        log.info("searchAndRateRepos {}", repoRequest);

        String query = queryService.generateQuery(repoRequest);

        List<GitHubRepositoryDto> allRepos = fetchAllPages(
                query,
                repoRequest.getRepoSearchSortBy(),
                repoRequest.getRepoSearchSortOrder(),
                personalToken);

        List<SearchRepositoryResponse> repositoryList = sortAndFilter(
                mapRepositories(allRepos), repoRequest.getMinimumScore(), repoRequest.getLimit());

        return new SearchResponse(repositoryList.size(), repositoryList);
    }

    private List<GitHubRepositoryDto> fetchAllPages(String query, GitHubSortBy sortBy,
                                                    GitHubSortOrder sortOrder,
                                                    String personalToken) {
        // We know that GitHub search API allows a maximum of 10 pages (100 items per page)
        List<Integer> pages = IntStream.rangeClosed(1, 10).boxed().toList();

        return Flux.fromIterable(pages)
                .parallel(10)  // Execute up to 10 pages in parallel
                .runOn(Schedulers.boundedElastic())  // Use boundedElastic for blocking calls
                .flatMap(page -> Mono.fromCallable(() -> {
                            GitHubSearchDto response = gitHubClient.searchRepos(
                                    query, page, 100, sortBy, sortOrder, personalToken);
                            return response.getItems() != null ? response.getItems() : List.<GitHubRepositoryDto>of();
                        })
                )
//                .flatMap(page ->
//                        Mono.fromCallable(() -> {
//                                    GitHubSearchDto response = gitHubClient.searchRepos(
//                                            query, page, 100, sortBy, sortOrder, personalToken);
//                                    return response.getItems() != null ? response.getItems() : List.<GitHubRepositoryDto>of();
//                                })
//                                .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
//                                        .maxBackoff(Duration.ofSeconds(5))
//                                        .doBeforeRetry(retrySignal ->
//                                                log.warn("Retrying page {} due to: {}", page, retrySignal.failure().getMessage()))
//                                )
//                                .onErrorResume(ex -> {
//                                    log.warn("Failed to fetch page {} after retries: {}", page, ex.getMessage());
//                                    return Mono.just(List.<GitHubRepositoryDto>of());
//                                })
//                )
                .sequential()
                .flatMap(Flux::fromIterable)
                .collectList()
                .block();
    }

    private List<SearchRepositoryResponse> sortAndFilter(List<SearchRepositoryResponse> repoList, Integer minimumScore, Integer limit) {
        Stream<SearchRepositoryResponse> stream = repoList.stream()
                .filter(repo -> minimumScore == null
                        || repo.getScore() >= minimumScore)
                .sorted(Comparator.comparingInt(SearchRepositoryResponse::getScore).reversed());

        if (limit != null) {
            stream = stream.limit(limit);
        }

        return stream.toList();
    }

    private List<SearchRepositoryResponse> mapRepositories(List<GitHubRepositoryDto> dtos) {
        if (dtos == null) {
            return new ArrayList<>();
        }
        return dtos.stream()
                .map(dto -> repositoryMapper.toResponse(dto, calcScore(dto)))
                .toList();
    }

    private Integer calcScore(GitHubRepositoryDto dto) {
        return scoreService.calcScore(dto.getCreatedAt(), dto.getUpdatedAt(), dto.getForksCount(), dto.getStargazersCount());
    }

}
