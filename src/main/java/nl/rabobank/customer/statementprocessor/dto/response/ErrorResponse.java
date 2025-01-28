package nl.rabobank.customer.statementprocessor.dto.response;

public record ErrorResponse(
        String error,
        String details) {
}