package nl.rabobank.customer.statementprocessor.exception;

import nl.rabobank.customer.statementprocessor.dto.response.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for the application.
 * This class handles various exceptions thrown by controllers and generates appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileParsingException.class)
    public ResponseEntity<ErrorResponse> handleFileParsingException(FileParsingException e) {
        ErrorResponse errorResponse = new ErrorResponse("Error parsing the file", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
    /**
     * Handles InvalidFileException by returning a bad request response with the error message.
     *
     * @param e The InvalidFileException thrown during file processing.
     * @return A ResponseEntity containing an error message and HTTP status 400.
     */
    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFileException(InvalidFileException e) {
        ErrorResponse errorResponse = new ErrorResponse("Invalid file", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * Handles general exceptions by returning an internal server error response with the error message.
     *
     * @param e The generic exception thrown during processing.
     * @return A ResponseEntity containing an error message and HTTP status 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse("File processing failed", e.getLocalizedMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    /**
     * Handles MethodArgumentNotValidException thrown when validation fails for method arguments.
     *
     * @param ex The MethodArgumentNotValidException containing the validation errors.
     * @return A ResponseEntity containing a list of error messages and HTTP status 400.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        String errorMessage = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse("File processing failed", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}