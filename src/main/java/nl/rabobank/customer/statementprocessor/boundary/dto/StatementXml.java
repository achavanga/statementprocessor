package nl.rabobank.customer.statementprocessor.boundary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record StatementXml(
        @JacksonXmlProperty(isAttribute = true) @JsonProperty("reference") @NotNull long reference,
        @JacksonXmlProperty @JsonProperty("accountNumber") @NotNull String accountNumber,
        @JacksonXmlProperty @JsonProperty("description") @NotNull String description,
        @JacksonXmlProperty @JsonProperty("startBalance") @NotNull BigDecimal startBalance,
        @JacksonXmlProperty @JsonProperty("mutation") @NotNull BigDecimal mutation,
        @JacksonXmlProperty @JsonProperty("endBalance") @NotNull BigDecimal endBalance
) {
}