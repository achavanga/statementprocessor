package nl.rabobank.customer.statementprocessor.boundary.dto.response;

public record ErrorResponse(
        String error,
        String details) {
}