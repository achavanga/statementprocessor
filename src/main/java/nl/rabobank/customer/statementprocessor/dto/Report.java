package nl.rabobank.customer.statementprocessor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response object for report generation")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Report(
        @Schema(description = "Unique report ID") Long reportId,
        @Schema(description = "List of failed records")  List<ValidationResult> failedRecords) {}
