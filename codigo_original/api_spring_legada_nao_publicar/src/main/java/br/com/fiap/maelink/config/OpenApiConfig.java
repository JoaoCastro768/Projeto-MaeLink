package br.com.fiap.maelink.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI maeLinkOpenApi() {
        return new OpenAPI().info(new Info()
                .title("MãeLink API")
                .version("v1")
                .description("API REST para cadastro, triagem, bancos de leite, agendamentos, doações, notificações e auditoria do ecossistema MãeLink.")
                .contact(new Contact().name("João Castro - RM 554628")));
    }
}
