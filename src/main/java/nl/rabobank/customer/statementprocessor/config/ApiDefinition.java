package nl.rabobank.customer.statementprocessor.config;


import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static nl.rabobank.customer.statementprocessor.util.StatementConstants.CUSTOMER_STATEMENT_PROCESSOR_API;

/**
 * Configuration class for defining the OpenAPI specification for the customer statement processor API.
 * This includes API versioning, title, and the paths that the API documentation will cover.
 */
@Configuration
public class ApiDefinition {

    /**
     * Defines a GroupedOpenApi bean that customizes the OpenAPI specification for the processor API.
     *
     * @return a GroupedOpenApi instance with specific metadata for the customer statement processor API.
     */
    @Bean
    public GroupedOpenApi controllerApi() {
        return GroupedOpenApi.builder()
                .group("processor")
                .displayName(CUSTOMER_STATEMENT_PROCESSOR_API)
                .addOpenApiCustomizer(openApi ->
                        openApi.setInfo(new Info()
                                .title(CUSTOMER_STATEMENT_PROCESSOR_API)
                                .version("1.8.0")))
                .pathsToMatch("/api/v1/statements/**")
                .build();
    }
}