package de.lindemann.redcare.mapper;

import de.lindemann.redcare.client.github.dto.GitHubRepositoryDto;
import de.lindemann.redcare.controller.repo.dto.SearchRepositoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class RepoMapperTest {

    private RepoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(RepoMapper.class);
    }

    @Test
    void toResponse_shouldMapAllFieldsCorrectly() {
        // given
        GitHubRepositoryDto dto = new GitHubRepositoryDto();
        dto.setId(123456L);
        dto.setFullName("spring-projects/spring-boot");
        dto.setHtmlUrl("https://github.com/spring-projects/spring-boot");
        dto.setLanguage("Java");
        dto.setDescription("Spring Boot makes it easy to create stand-alone, production-grade Spring applications.");
        dto.setCreatedAt(Instant.parse("2014-04-01T00:00:00Z"));
        dto.setUpdatedAt(Instant.parse("2025-10-16T00:00:00Z"));
        dto.setStargazersCount(68000);
        dto.setForksCount(38000);
        dto.setAllowForking(true);

        int score = 999;

        // when
        SearchRepositoryResponse response = mapper.toResponse(dto, score);

        // then
        assertNotNull(response);
        assertEquals(dto.getId(), response.getGitHubId());
        assertEquals(dto.getFullName(), response.getFullName());
        assertEquals(dto.getHtmlUrl(), response.getHtmlUrl());
        assertEquals(dto.getLanguage(), response.getLanguage());
        assertEquals(dto.getDescription(), response.getDescription());
        assertEquals(dto.getCreatedAt(), response.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), response.getUpdatedAt());
        assertEquals(dto.getStargazersCount(), response.getStargazersCount());
        assertEquals(dto.getForksCount(), response.getForksCount());
        assertEquals(dto.isAllowForking(), response.isAllowForking());
        assertEquals(score, response.getScore());
    }

    @Test
    void toResponse_shouldHandleNullValuesGracefully() {
        // given
        GitHubRepositoryDto dto = new GitHubRepositoryDto();
        dto.setId(null);
        dto.setFullName(null);
        dto.setHtmlUrl(null);
        dto.setLanguage(null);
        dto.setDescription(null);
        dto.setCreatedAt(null);
        dto.setUpdatedAt(null);
        dto.setStargazersCount(null);
        dto.setForksCount(null);
        dto.setAllowForking(false);

        Integer score = null;

        // when
        SearchRepositoryResponse response = mapper.toResponse(dto, score);

        // then
        assertNotNull(response);
        assertNull(response.getGitHubId());
        assertNull(response.getFullName());
        assertNull(response.getHtmlUrl());
        assertNull(response.getLanguage());
        assertNull(response.getDescription());
        assertNull(response.getCreatedAt());
        assertNull(response.getUpdatedAt());
        assertNull(response.getStargazersCount());
        assertNull(response.getForksCount());
        assertFalse(response.isAllowForking());
        assertNull(response.getScore());
    }
}
