package de.lindemann.redcare.adapter;

import de.lindemann.redcare.controller.repo.dto.RepoLanguageResponse;
import de.lindemann.redcare.controller.repo.dto.SearchRequest;
import de.lindemann.redcare.controller.repo.dto.SearchResponse;
import de.lindemann.redcare.service.repo.RepoLanguageService;
import de.lindemann.redcare.service.repo.RepoSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepoAdapter {

    private final RepoSearchService repoService;
    private final RepoLanguageService languageService;

    public SearchResponse search(SearchRequest repoRequest, String personalToken) {
        return repoService.searchAndRateRepos(repoRequest, personalToken);
    }

    public RepoLanguageResponse getLanguages() {
        List<String> languages = languageService.getProgrammingLanguages();
        return new RepoLanguageResponse(languages);
    }
}
