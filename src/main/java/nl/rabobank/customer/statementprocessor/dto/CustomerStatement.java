package nl.rabobank.customer.statementprocessor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerStatement(
        @NotNull Long reference,
        @NotNull String accountNumber,
        @NotNull BigDecimal startBalance,
        @NotNull BigDecimal mutation,
        @NotNull String description,
        @NotNull BigDecimal endBalance
) {}