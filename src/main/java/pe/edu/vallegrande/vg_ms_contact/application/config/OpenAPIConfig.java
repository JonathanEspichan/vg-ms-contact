package pe.edu.vallegrande.vg_ms_contact.application.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI userServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("API de Servicio de Contactanos")
                        .description("Esta es la API REST para el Servicio de Contactanos")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación Wiki del Servicio de Contactanos")
                        .url("https://vallegrande.edu.pe/docs"));
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}