package de.lindemann.redcare.controller.repo;

import de.lindemann.redcare.client.github.GitHubSearchDto;
import lombok.Data;

@Data
public class RepoResponse {

    private String test;
    private GitHubSearchDto search;
}
