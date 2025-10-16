package de.lindemann.redcare.mapper;

import de.lindemann.redcare.client.github.dto.GitHubRepositoryDto;
import de.lindemann.redcare.controller.repo.dto.SearchRepositoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class RepoMapper {

    @Mapping(source = "dto.id", target = "gitHubId")
    public abstract SearchRepositoryResponse toResponse(GitHubRepositoryDto dto, Integer score);
}
