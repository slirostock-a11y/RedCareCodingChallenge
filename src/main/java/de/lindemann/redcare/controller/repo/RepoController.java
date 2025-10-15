package de.lindemann.redcare.controller.repo;

import de.lindemann.redcare.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RepoController {

    private final GitHubService gitHubService;

    @GetMapping("/hello")
    public RepoResponse hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return gitHubService.searchAndRateRepos(new RepoRequest());
    }
}
