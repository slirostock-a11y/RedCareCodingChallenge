package de.lindemann.redcare.service.repo;

import de.lindemann.redcare.client.github.GitHubApiClient;
import de.lindemann.redcare.client.github.GitHubSearchDto;
import de.lindemann.redcare.controller.repo.RepoRequest;
import de.lindemann.redcare.controller.repo.RepoResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepoService {

    private final GitHubApiClient gitHubClient;
    private final RepoScoreService repoScoreService;

    public RepoResponse searchAndRateRepos(@NotNull RepoRequest repoRequest) {
        log.info("searchAndRateRepos {}", repoRequest);
        String query = generateQuery(repoRequest);
        GitHubSearchDto gitHubResponse = gitHubClient.searchRepos("q", 1);
        RepoResponse response = new RepoResponse();
        response.setSearch(gitHubResponse);
        return response;
    }

    private String generateQuery(RepoRequest repoRequest) {
        String language = generateLanguage(repoRequest.getLanguage());
        return language;
    }

    private String generateLanguage(String language) {
        if (StringUtils.isBlank(language)) {
            return StringUtils.EMPTY;
        }

        if (language.matches(".*[#\\+].*")) {
            return "language:" + URLEncoder.encode(language, StandardCharsets.UTF_8);
        }

        if (language.contains(" ") || language.contains("-")) {
            String quoted = "\"" + language + "\"";
            return "language:" + URLEncoder.encode(quoted, StandardCharsets.UTF_8);
        }

        return "language:" + language;
    }
}
