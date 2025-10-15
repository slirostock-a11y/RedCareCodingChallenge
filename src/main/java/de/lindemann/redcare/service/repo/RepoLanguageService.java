package de.lindemann.redcare.service.repo;

import de.lindemann.redcare.client.github.GitHubRawClient;
import de.lindemann.redcare.controller.repo.RepoLanguageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepoLanguageService {
    private final GitHubRawClient rawClient;

    public RepoLanguageResponse getProgrammingLanguages() {
        List<String> languages = rawClient.getProgrammingLanguages();
        return new RepoLanguageResponse(languages);
    }
}
