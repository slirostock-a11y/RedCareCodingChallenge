package de.lindemann.redcare.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Stefan Lindemann");
        myContact.setEmail("stefanlindemann@yahoo.de");

        Info information = new Info()
                .title("RedCare CodingChallenge API")
                .version("0.0.1")
                .description("""
                        Backend application for scoring repositories on GitHub.
                        
                        The scoring Algorithm uses stars, forks, recency of updates.
                        """)
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
