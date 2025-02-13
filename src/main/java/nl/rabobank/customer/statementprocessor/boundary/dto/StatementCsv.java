package nl.rabobank.customer.statementprocessor.boundary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.math.BigDecimal;

/**
 * Represents a statement record in CSV format.
 * This class is annotated with Jackson annotations to map CSV fields to Java properties.
 */
@JacksonXmlRootElement
@JsonPropertyOrder({"Reference", "Account Number", "Description", "Start Balance", "Mutation", "End Balance"})
public record StatementCsv(
        @JsonProperty("Reference") long reference,
        @JsonProperty("Account Number") String accountNumber,
        @JsonProperty("Description") String description,
        @JsonProperty("Start Balance") BigDecimal startBalance,
        @JsonProperty("Mutation") BigDecimal mutation,
        @JsonProperty("End Balance") BigDecimal endBalance
) {}