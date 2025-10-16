package de.lindemann.redcare.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI defineOpenApi(@Value("${openapi.server-url}") String url, @Value("${openapi.server-description}") String description) {
        Server server = new Server();
        server.setUrl(url);
        server.setDescription(description);

        Contact myContact = new Contact();
        myContact.setName("Homer Incognito");
        myContact.setEmail("dontWriteMe@nonExisting.de");

        Info information = new Info()
                .title("RedCare CodingChallenge API")
                .version("v1")
                .description("""
                        Backend application for searching and scoring GitHub repositories.
                        
                        This service allows users to search public GitHub repositories with advanced filters, including programming language, creation date, and sorting options.\s
                        It computes a custom score for each repository based on:
                        
                        - Recency of updates (hotness)
                        - Continuous activity over time (steady contributions)
                        - Number of forks (logarithmic scale)
                        - Number of stars (logarithmic scale)
                        
                        The GitHub API search results are limited to 1000 repositories per query.\s
                        To overcome rate limits, users can optionally provide their own GitHub Personal Access Token (PAT) via the `X-GitHub-Token` request header.\s
                        If no token is provided, the server uses its default token.
                        
                        Additional features:
                        
                        - Filtering by minimum computed score
                        - Limiting the number of returned repositories
                        - Sorting by stars, forks, help-wanted-issues, or updated timestamp
                        - Automatic pagination handling to fetch all available results up to the GitHub API limit
                        
                        All responses include repository details along with the computed score.\s
                        This allows clients to retrieve the most relevant and active repositories efficiently.
                        """)
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}