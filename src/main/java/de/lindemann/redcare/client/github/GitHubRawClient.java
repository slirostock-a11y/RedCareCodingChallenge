package de.lindemann.redcare.client.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.Yaml;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GitHubRawClient {
    private final WebClient webClient;

    public GitHubRawClient(WebClient.Builder builder,
                           @Value("${githubraw.url}") String url) {

        this.webClient = builder
                .baseUrl(url)
                .defaultHeader(HttpHeaders.ACCEPT_ENCODING, "gzip")
                .build();
    }

    @Cacheable("gitHubLanguages")
    public List<String> getProgrammingLanguages() {

        String yamlContent = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("github/linguist/main/lib/linguist/languages.yml")
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new RuntimeException("Page not found!")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new RuntimeException("GitHub server error")))
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "GitHub raw not found")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> Mono.error(new ResponseStatusException(
                                HttpStatus.FAILED_DEPENDENCY, "GitHub raw not accessible")))
                .bodyToMono(String.class)
                .block();

        Yaml yaml = new Yaml();
        Map<String, Map<String, Object>> data = yaml.load(yamlContent);

        return data.entrySet().stream()
                .filter(entry -> {
                    Map<String, Object> props = (Map<String, Object>) entry.getValue();
                    Object type = props.get("type");
                    return "programming".equalsIgnoreCase(String.valueOf(type));
                })
                .map(Map.Entry::getKey)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();
    }
}
