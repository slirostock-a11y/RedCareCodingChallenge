package de.lindemann.redcare;

import de.lindemann.redcare.controller.repo.dto.RepoLanguageResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void languageEndpointShouldReturnGreeting() {
        String url = "http://localhost:" + port + "/api/repository/languages";
        ResponseEntity<RepoLanguageResponse> response = restTemplate.getForEntity(url, RepoLanguageResponse.class);

        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Assertions.assertThat(response.getBody().getLanguages().size()).isGreaterThan(1);
    }

}
