package de.lindemann.redcare.client.github;

import lombok.Data;

import java.util.List;

@Data
public class GitHubSearchDto {

    private Integer totalCount;

    private boolean incompleteResults;

    private List<GitHubRepositoryDto> items;

}
