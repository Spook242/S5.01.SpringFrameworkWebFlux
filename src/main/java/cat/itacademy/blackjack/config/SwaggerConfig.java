package cat.itacademy.blackjack.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI blackjackOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Blackjack API - IT Academy")
                        .description("API reactiva per gestionar partides de Blackjack amb MongoDB i MySQL.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("El teu Nom")
                                .email("teuemail@exemple.com")));
    }
}