package de.lindemann.redcare.service;

import de.lindemann.redcare.client.github.GitHubClient;
import de.lindemann.redcare.client.github.GitHubSearchDto;
import de.lindemann.redcare.controller.repo.RepoRequest;
import de.lindemann.redcare.controller.repo.RepoResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubService {

    private final GitHubClient gitHubClient;
    private final GitHubScoreService gitHubScoreService;

    public RepoResponse searchAndRateRepos(@NotNull RepoRequest repoRequest) {
        log.info("searchAndRateRepos {}", repoRequest);
        GitHubSearchDto gitHubResponse = gitHubClient.searchRepos("q", 1);
        RepoResponse response = new RepoResponse();
        response.setSearch(gitHubResponse);
        return response;
    }
}
