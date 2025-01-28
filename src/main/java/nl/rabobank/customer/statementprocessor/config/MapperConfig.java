package nl.rabobank.customer.statementprocessor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import nl.rabobank.customer.statementprocessor.mapper.CustomerStatementToStatementMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration class for defining and providing various mappers used in the application.
 * These mappers include JSON, XML, and CSV mappers for processing customer statement data.
 */
@Configuration
public class MapperConfig {

    /**
     * Provides the XmlMapper bean for XML processing.
     *
     * @return a configured XmlMapper instance.
     */
    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }

    /**
     * Provides the CsvMapper bean for CSV processing.
     *
     * @return a configured CsvMapper instance.
     */
    @Bean
    public CsvMapper csvMapper() {
        return new CsvMapper();
    }

    /**
     * Provides the CustomerStatementToStatementMapper bean for mapping between customer statements.
     *
     * @return a configured CustomerStatementToStatementMapper instance.
     */
    @Bean
    public CustomerStatementToStatementMapper customerStatementToStatementMapper() {
        return new CustomerStatementToStatementMapper();
    }

    /**
     * Provides the ObjectMapper bean for JSON processing.
     * This bean is marked as the primary bean to be used for JSON mapping in the application.
     *
     * @return a configured ObjectMapper instance.
     */
    @Bean
    @Primary
    public ObjectMapper jsonMapper() {
        return new ObjectMapper();
    }

}