package nl.rabobank.customer.statementprocessor.control.exception;

public class InvalidFileException extends RuntimeException {
    public InvalidFileException(String message) {
        super(message);
    }
}