package nl.rabobank.customer.statementprocessor.boundary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Validation result for a failed record")
public record ValidationResult(
        @Schema(description = "Transaction reference") Long reference,
        @Schema(description = "Transaction description") String description,
        @Schema(description = "Error message") String errorMessage
) {}

