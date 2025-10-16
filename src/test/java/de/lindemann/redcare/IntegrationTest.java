package de.lindemann.redcare;

import de.lindemann.redcare.client.github.GitHubApiClient;
import de.lindemann.redcare.client.github.GitHubRawClient;
import de.lindemann.redcare.client.github.dto.GitHubRepositoryDto;
import de.lindemann.redcare.client.github.dto.GitHubSearchDto;
import de.lindemann.redcare.controller.repo.dto.RepoLanguageResponse;
import de.lindemann.redcare.controller.repo.dto.SearchResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private GitHubApiClient gitHubApiClient;

    @MockBean
    private GitHubRawClient gitHubRawClient;

    @LocalServerPort
    private int port;

    @Test
    void languageEndpointShouldReturnSomething() {
        Mockito.when(gitHubRawClient.getProgrammingLanguages())
                .thenReturn(Arrays.asList("Java", "Python", "C#"));

        String url = "http://localhost:" + port + "/api/repository/languages";
        ResponseEntity<RepoLanguageResponse> response = restTemplate.getForEntity(url, RepoLanguageResponse.class);

        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getBody().getLanguages().size()).isGreaterThan(1);
    }

    @Test
    void searchEndpointShouldReturnSomething() {
        GitHubSearchDto mockDto = new GitHubSearchDto();
        mockDto.setTotalCount(2);
        mockDto.setItems(Arrays.asList(
                GitHubRepositoryDto.builder().id(1L).fullName("repo1").language("Java")
                        .updatedAt(Instant.now()).createdAt(Instant.now()).stargazersCount(0).forksCount(0).build(),
                GitHubRepositoryDto.builder().id(2L).fullName("repo2").language("Python")
                        .updatedAt(Instant.now()).createdAt(Instant.now()).stargazersCount(0).forksCount(0).build()
        ));

        Mockito.when(gitHubApiClient.searchRepos(
                        Mockito.anyString(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.any()))
                .thenReturn(mockDto);

        String url = "http://localhost:" + port + "/api/repository/search?q=test";

        ResponseEntity<SearchResponse> response = restTemplate.getForEntity(url, SearchResponse.class);

        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getBody().getSize()).isEqualTo(2);
        Assertions.assertThat(response.getBody().getRepositories().size()).isEqualTo(2);
    }

}
