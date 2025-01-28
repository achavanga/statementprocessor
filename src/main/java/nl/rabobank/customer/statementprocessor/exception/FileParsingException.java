package nl.rabobank.customer.statementprocessor.exception;

public class FileParsingException extends RuntimeException {
    public FileParsingException(String message) {
        super(message);
    }
}