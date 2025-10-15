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
                        Backend application for scoring repositories on GitHub.
                        
                        The scoring algorithm uses stars, forks, recency of updates.
                        
                        The GitHub-API-response is limited to 1000 entries.
                        """)
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}