package nl.rabobank.customer.statementprocessor.control.exception;

public class FileParsingException extends RuntimeException {
    public FileParsingException(String message) {
        super(message);
    }
}